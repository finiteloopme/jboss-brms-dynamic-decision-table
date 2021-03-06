package demo.finiteloop.me.model;

/**
 * This class was automatically generated by the data modeler tool.
 */
public class DriverProfile  implements java.io.Serializable {

static final long serialVersionUID = 1L;
    
    @org.kie.api.definition.type.Position(value = 1)
    private java.lang.Integer age;
    
    @org.kie.api.definition.type.Position(value = 3)
    private java.lang.String gender;
    
    @org.kie.api.definition.type.Position(value = 4)
    private java.lang.Boolean hasPreviousIncidents;
    
    @org.kie.api.definition.type.Position(value = 0)
    private java.lang.String name;
    
    @org.kie.api.definition.type.Position(value = 2)
    private java.lang.String state;

    public DriverProfile() {
    }

    public DriverProfile(java.lang.String name, java.lang.Integer age, java.lang.String state, java.lang.String gender, java.lang.Boolean hasPreviousIncidents) {
        this.name = name;
        this.age = age;
        this.state = state;
        this.gender = gender;
        this.hasPreviousIncidents = hasPreviousIncidents;
    }


    
    public java.lang.Integer getAge() {
        return this.age;
    }

    public void setAge(  java.lang.Integer age ) {
        this.age = age;
    }
    
    public java.lang.String getGender() {
        return this.gender;
    }

    public void setGender(  java.lang.String gender ) {
        this.gender = gender;
    }
    
    public java.lang.Boolean getHasPreviousIncidents() {
        return this.hasPreviousIncidents;
    }

    public void setHasPreviousIncidents(  java.lang.Boolean hasPreviousIncidents ) {
        this.hasPreviousIncidents = hasPreviousIncidents;
    }
    
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(  java.lang.String name ) {
        this.name = name;
    }
    
    public java.lang.String getState() {
        return this.state;
    }

    public void setState(  java.lang.String state ) {
        this.state = state;
    }
}