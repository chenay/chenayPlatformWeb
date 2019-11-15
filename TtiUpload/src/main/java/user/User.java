package user;


public class User {
    /**
     * id : 1
     * name : DBG
     * date : null
     * salary : 5000
     * remark : 备注
     */

    public String StaffNumber;
    public String Name;
    public String Role;
    /**
     * Password : 123456
     */

    public String Password;
    /**
     * createDate :
     * updateDate :
     */

    public String createDate;
    public String updateDate;


    @Override
    public String toString() {
        return "User{" +
                "StaffNumber='" + StaffNumber + '\'' +
                ", Name='" + Name + '\'' +
                ", Role='" + Role + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }

    /**
     * StaffNumber : 123
     * Name : DBG
     * Role : MIS_DBG,MISDBG2
     */
    public String getStaffNumber() {
        return StaffNumber;
    }

    public void setStaffNumber(String StaffNumber) {
        this.StaffNumber = StaffNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
