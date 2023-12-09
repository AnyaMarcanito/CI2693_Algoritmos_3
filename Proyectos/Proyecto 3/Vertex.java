class Vertex<T> {
    private T value;
    private int height;
    private int inwardDegree;
    private boolean spills;
    private int f;

    public Vertex(T value) {
        this.value = value;
        this.height = 0;
        this.inwardDegree = 0;
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

    public void upInwardDegree() {
        this.inwardDegree = inwardDegree + 1;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "Vertex{value=" + value + ", height=" + height + "}";
    }
}
