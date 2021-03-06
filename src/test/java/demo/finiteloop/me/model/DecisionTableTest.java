/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.finiteloop.me.model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

/**
 * @author Kunal Limaye
 * 
 */
public class DecisionTableTest {

    private static StatelessKieSession kSession;

    private static final String QUOTATION_GLOBAL = "$priceQuotation";
    private static final String GENDER_MALE = "MALE";
    private static final String GENDER_FEMALE = "FEMALE";

    @BeforeClass
    public static void setup() throws URISyntaxException, IOException {
    	URL urlExtDecisionTable = new URL("https://github.com/finiteloopme/jboss-brms-dynamic-decision-table/raw/ext-decision-table/insurance-rules.xls"); 	
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        SpreadsheetCompiler sc = new SpreadsheetCompiler();
//        System.out.println("File name: " + urlExtDecisionTable.getFile());
        Resource externalDecisionTable = kieServices.getResources().newInputStreamResource(urlExtDecisionTable.openConnection().getInputStream());
        kfs.write("src/main/resources/" + urlExtDecisionTable.getFile(), externalDecisionTable);
        KieBuilder kbuilder = kieServices.newKieBuilder(kfs);
        kbuilder.buildAll();
        KieRepository kieRepo = kieServices.getRepository();
        KieContainer kContainer = kieServices.newKieContainer(kieRepo.getDefaultReleaseId());
        kSession = kContainer.newStatelessKieSession();
    }

    @Before
    public void beforeTest() throws InstantiationException, IllegalAccessException {
        PriceQuotation priceQuotation = new PriceQuotation();
        kSession.setGlobal(QUOTATION_GLOBAL, priceQuotation);
    }

    @Test
    public void testRuleStandardJunior() {
        System.out.println("** Testing Standard Junior Rule **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(true);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(20);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(false);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(1800, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardJuniorRisk1() {
        System.out.println("** Testing Standard Junior Rule with Risk 1 (no garage, no alarm) **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(20);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(false);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(2000, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardJuniorRisk2() {
        System.out.println("** Testing Standard Junior Rule with Risk 2 (no garage, no alarm - previous incidents) **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(20);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(true);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(2500, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardYoungLady1() {
        System.out.println("** Testing Young Lady 1 (promotional value) **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(20);
        driverProfile.setGender(GENDER_FEMALE);
        driverProfile.setHasPreviousIncidents(true);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(1500, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardM1() {
        System.out.println("** Testing standard price for Males with more than 21 years old **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(30);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(false);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(1000, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardM1Risk() {
        System.out.println("** Testing standard price for Males with more than 21 years old with risk (previous incidents) **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(30);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(true);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(1100, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleStandardF() {
        System.out.println("** Testing Female with more than 21 years old **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(30);
        driverProfile.setGender(GENDER_FEMALE);
        driverProfile.setHasPreviousIncidents(true);
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(900, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleSeniorStatePromotion() {
        System.out.println("** Testing senior from NY or NC **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(60);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(true);
        driverProfile.setState("NC");
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(200, priceQuotation.getPrice().intValue());
    }

    @Test
    public void testRuleSeniorStatePromotionPlus() {
        System.out.println("** Testing senior from NY or NC - special price for no incidents **");
        CarProfile carProfile = new CarProfile();
        carProfile.setHasAlarm(false);
        carProfile.setHasGarage(false);
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setAge(60);
        driverProfile.setGender(GENDER_MALE);
        driverProfile.setHasPreviousIncidents(false);
        driverProfile.setState("NC");
        List<Serializable> objects = new ArrayList<Serializable>();
        objects.add(carProfile);
        objects.add(driverProfile);
        kSession.execute(objects);
        PriceQuotation priceQuotation = (PriceQuotation) kSession.getGlobals().get(QUOTATION_GLOBAL);
        System.out.println("Resulting price: " + priceQuotation.getPrice());
        assertEquals(100, priceQuotation.getPrice().intValue());
    }
}
