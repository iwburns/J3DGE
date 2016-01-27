package engine.object3d;

import engine.object3d.light.Light;

import java.util.ArrayList;

public class Scene extends Object3d {

    //TODO: write a destroy

    protected ArrayList<Light> lights;

    public Scene() {
        super();
        lights = new ArrayList<>();
    }

    public void add(Object3d obj) {
        addChild(obj);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public boolean remove(Object3d obj) {
        return children.remove(obj);
    }

    public boolean remove(Light light) {
        return lights.remove(light);
    }

    public ArrayList<Object3d> getObjects() {
        return children;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

}
