/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Control;

/**
 *
 * @author Leandro Liu
 */
public class inercialControl {

    public byte LSpeed;
    public byte RSpeed;
    private byte inertia=10;

    inercialControl(){
        LSpeed=0;
        RSpeed=0;
    }

    public void setSpeed(byte L, byte R){
        LSpeed=L;
        RSpeed=R;
    }

    public void addLSpeed(){
        if(!(LSpeed>=Byte.MAX_VALUE-inertia))
        LSpeed+=inertia;
    }
    public void addRSpeed(){
        if(!(RSpeed>=Byte.MAX_VALUE-inertia))
        RSpeed+=inertia;
    }
    public void subLSpeed(){
        if(!(LSpeed<=Byte.MIN_VALUE-inertia))
        LSpeed-=inertia;
    }
    public void subRSpeed(){
        if(!(RSpeed<=Byte.MIN_VALUE-inertia))
        RSpeed-=inertia;
    }
    public void stop(){
        LSpeed=0;
        RSpeed=0;
    }

    public void addSpeed(){
        addLSpeed();
        addRSpeed();
    }

    void updateLow() {
        if(LSpeed>0){
            LSpeed-=inertia;
        }else if(LSpeed<0){
            LSpeed+=inertia;

        }
        if(RSpeed>0){
            RSpeed-=inertia;
        }else if(RSpeed<0){
            RSpeed+=inertia;
        }
    }

    void subSpeed() {
        subLSpeed();
        subRSpeed();
    }

}
