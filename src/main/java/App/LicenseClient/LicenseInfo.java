package App.LicenseClient;

public class LicenseInfo {

    //TODO Remove souts from setters
    public void setId(String id) {
        this.id = id;
        System.out.println("set id is : "+id);
    }


    public void setEmail(String email) {
        this.email = email;
        System.out.println("set email is : "+email);
    }


    public void setCompany(String company) {
        this.company = company;
        System.out.println("set company name is : "+company);
    }
        //TODO Remove getters from this class after debugging etc,
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    private String id;
    private String email;
    private String company;
}
