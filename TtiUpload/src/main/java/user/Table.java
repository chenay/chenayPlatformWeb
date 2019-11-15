package user;

public class Table {
    /**
     * tablename : MIS_DBG
     * attributes : id
     * type : number
     * remark : 上传用户表
     */

    public String tablename;
    public String attributes;
    public String type;
    public String nullable;
    public String remark;
    /**
     * length : length
     */


    public String length;
    /**
     * ps : ps
     */

    public String ps;


    /**
     * database : database
     */

    public String database;

    public Table(String tablename, String attributes, String type, String nullable, String remark, String length, String ps, String database, int tableId) {
        this.tablename = tablename;
        this.attributes = attributes;
        this.type = type;
        this.nullable = nullable;
        this.remark = remark;
        this.length = length;
        this.ps = ps;
        this.database = database;
        this.tableId = tableId;

    }

    @Override
    public String toString() {
        return "Table{" +
                "tablename='" + tablename + '\'' +
                ", attributes='" + attributes + '\'' +
                ", type='" + type + '\'' +
                ", nullable='" + nullable + '\'' +
                ", remark='" + remark + '\'' +
                ", length='" + length + '\'' +
                ", ps='" + ps + '\'' +
                ", database='" + database + '\'' +
                ", tableId=" + tableId +
                '}';
    }

    /**
     * tableId : 1
     */

    public int tableId;


    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) { this.nullable = nullable; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Table(String tablename, String attributes, String type, String nullable, String remark, String database , String length, String ps) {
        this.tablename = tablename;
        this.attributes = attributes;
        this.type = type;
        this.nullable = nullable;
        this.remark = remark;
        this.database = database;
        this.length = length;
        this.ps = ps;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Table() {
    }



    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
