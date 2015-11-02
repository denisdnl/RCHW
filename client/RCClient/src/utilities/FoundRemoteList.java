/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;

/**
 *
 * @author Deni-W7
 */
public class FoundRemoteList {
    public static ArrayList<String> hosts;
    public FoundRemoteList(){
        if(hosts == null)
            hosts = new ArrayList<String>();
    }
}
