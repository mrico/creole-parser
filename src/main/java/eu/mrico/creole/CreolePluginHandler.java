/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.mrico.creole;

import eu.mrico.creole.ast.Element;
import java.util.Map;

/**
 *
 * @author marcorico-gomez
 */
public interface CreolePluginHandler {

    String getName();

    Element execute(Map<String, String> args);

}
