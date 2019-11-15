package user;

import java.util.Date;

public class Note {
    /**
     * id : id
     * fileName : fileName
     * uploadBy : uploadBy
     * uploadDate : loadDate
     * tablename : tablename
     */

    private String id;
    private String fileName;
    private String uploadBy;
    private Date uploadDate;
    private String tablename;
    /**
     * database : database
     */

    private String database;

    public Note() {
    }

    public Note(String id, String fileName, String uploadBy, Date uploadDate, String tablename, String database) {

        this.id = id;
        this.fileName = fileName;
        this.uploadBy = uploadBy;
        this.uploadDate = uploadDate;
        this.tablename = tablename;
        this.database = database;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadBy='" + uploadBy + '\'' +
                ", uploadDate=" + uploadDate +
                ", tablename='" + tablename + '\'' +
                ", database='" + database + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getRemark() {
        return tablename;
    }

    public void setRemark(String tablename) {
        this.tablename = tablename;
    }

    public Note(String id, String fileName, String uploadBy, Date uploadDate, String tablename) {
        this.id = id;
        this.fileName = fileName;
        this.uploadBy = uploadBy;
        this.uploadDate = uploadDate;
        this.tablename = tablename;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
