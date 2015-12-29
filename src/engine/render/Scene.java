package engine.render;

import engine.object3d.Object3d;

import java.util.ArrayList;

public class Scene {

    //TODO: write a destroy

    private ArrayList<Object3d> objects;

    public Scene() {
        objects = new ArrayList<>();
    }

    public void add(Object3d obj) {
        objects.add(obj);
    }

    public boolean remove(Object3d obj) {
        return objects.remove(obj);
    }

    public ArrayList<Object3d> getObjects() {
        return objects;
    }

}
