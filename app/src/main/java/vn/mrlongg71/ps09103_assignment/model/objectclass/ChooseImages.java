package vn.mrlongg71.ps09103_assignment.model.objectclass;

public class ChooseImages {
    String pathImages;
    boolean checked;

    public ChooseImages(String pathImages, boolean checked) {
        this.pathImages = pathImages;
        this.checked = checked;
    }

    public ChooseImages() {
    }

    public String getPathImages() {
        return pathImages;
    }

    public void setPathImages(String pathImages) {
        this.pathImages = pathImages;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
