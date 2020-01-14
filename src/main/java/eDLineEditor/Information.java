package eDLineEditor;

import java.util.ArrayList;

public class Information {
    private int now;
    private ArrayList<String> buffer;
      
    public Information() {  
        super();  
    }  
      
    public Information(int now, ArrayList<String> buffer) {  
        super();  
        this.now = now;  
        this.buffer = buffer;  
    }  
      
    public int getNow() {  
        return now;  
    }  
      
    public void setNow(Integer now) {  
        this.now = now;  
    }  
      
    public ArrayList<String> getBuffer() {  
        return buffer;  
    }  
      
    public void setBuffer(ArrayList<String> buffer) {  
        this.buffer = buffer;  
    }  
  
      
}  
