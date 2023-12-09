class Vertex<T> {
    private T value;
    private int height;
    private int inwardDegree;
    private int outwardDegree;
    private boolean spills;

    public Vertex(T value) {
        this.value = value;
        this.height = 0;
        this.inwardDegree = 0;
        this.outwardDegree = 0;
        this.spills = false;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public boolean isSpills() {
        return spills;
    }
    
    public void setSpills(boolean spills) {
        this.spills = spills;
    }

    public int getInwardDegree() {
        return inwardDegree;
    }

    public int getOutwardDegree() {
        return outwardDegree;
    }
}