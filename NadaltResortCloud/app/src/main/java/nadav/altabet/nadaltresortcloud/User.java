package nadav.altabet.nadaltresortcloud;

public class User {

    private String FirstName;
    private String LastName;
    private String Email;
    private String Gender;
    private String Pic;
    private Date DateOfBirth;
    private String Phone;
    //-----------------------------------------------------
    public User(String Email, String gender, String pic, String FirstName,String LastName,Date dateOfBirth,String Phone) {
        this.Email = Email;
        this.Gender = gender;
        this.Pic = pic;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.DateOfBirth = dateOfBirth;
        this.Phone = Phone;
    }
    public User()
    {

    }

    //------------------------------------------------
    public void setEmail(String email) {
        Email = email;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setPic(String pic) {
        this.Pic = pic;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.DateOfBirth = dateOfBirth;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    //----------------------------------------------
    public String getEmail() {
        return Email;
    }

    public String getGender() {
        return Gender;
    }

    public String getPic() {
        return Pic;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public String getPhone() {
        return Phone;
    }
    //----------------------------------------------
}

