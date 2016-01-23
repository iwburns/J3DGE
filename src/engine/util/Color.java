package engine.util;

public class Color {

    private float r;
    private float g;
    private float b;
    private float a;

    private Color(Color c) {
        this(c.r, c.g, c.b, c.a);
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Color(float r, float g, float b, float a) {
        setR(r);
        setG(g);
        setB(b);
        setA(a);
    }

    public Color add(Color c) {
        setR(this.r + c.r);
        setG(this.g + c.g);
        setB(this.b + c.b);
        setA(this.a + c.a);
        return this;
    }

    public Color mul(Color c) {
        this.r *= c.r;
        this.g *= c.g;
        this.b *= c.b;
        this.a *= c.a;
        return this;
    }

    public Color negate() {
        this.r = 1 - this.r;
        this.g = 1 - this.g;
        this.b = 1 - this.b;
        return this;
    }

    public Color clone() {
        return new Color(this);
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = clampToRange(r);
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = clampToRange(g);
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = clampToRange(b);
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = clampToRange(a);
    }

    private float clampToRange(float f) {
        return Math.min(1, Math.max(0, f));
    }
}
