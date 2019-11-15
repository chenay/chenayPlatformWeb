package user;

public class Database {
    /**
     * url : url
     * userName : userName
     * password : password
     */

    private String url;
    private String userName;
    private String password;
    /**
     * alias : alias
     */

    private String alias;
    /**
     * status : status
     */

    private String status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Database() {
    }

    public Database(String url, String userName, String password, String alias, String status) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.alias = alias;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Database{" +
                "url='" + url + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", alias='" + alias + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Database(String url, String userName, String password, String alias) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
