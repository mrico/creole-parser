/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.mrico.creole.plugins;

import eu.mrico.creole.CreolePluginHandler;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.Text;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author marcorico-gomez
 */
public class CurrentDateTimePlugin implements CreolePluginHandler {

    @Override
    public String getName() {
        return "CurrentDateTime";
    }

    @Override
    public Element execute(Map<String, String> args) {
        
        String now = DateFormat.getDateInstance().format(new Date());
        
        return new Text(now);
    }

    

}
