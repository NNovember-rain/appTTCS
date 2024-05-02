package vn.mrlongg71.ps09103_assignment.model.objectclass;

public class TypeBook {
    private String key,typecode,typename;

    public TypeBook(String key, String typecode, String typename) {
        this.key = key;
        this.typecode = typecode;
        this.typename = typename;
    }

    public TypeBook() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
