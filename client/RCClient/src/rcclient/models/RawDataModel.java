/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.models;

/**
 *
 * @author Deni-W7
 */
/*
Structure: 1 bytes start + 1 byte action code + 8 bytes length + data buffer
*/

public class RawDataModel {
    public long length;
    public byte[] data;
}
