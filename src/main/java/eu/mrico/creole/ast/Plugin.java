/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marcorico-gomez
 */
public class Plugin extends BaseElement {

    private String name;
    private Map<String, String> args;

    public String getName() {
        return name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public String getArg(String key) {
        return args.get(key);
    }

    public Plugin(String name) {
        this.name = name;
        this.args = new HashMap<String, String>();
    }

    public Plugin(String name, Map<String, String> args) {
        this.name = name;
        this.args = new HashMap<String, String>(args);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Plugin other = (Plugin) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Plugin[" + name + "]";
    }
}
