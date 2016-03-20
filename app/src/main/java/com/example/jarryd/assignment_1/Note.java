package com.example.jarryd.assignment_1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by jarryd on 20/03/16.
 */
public class Note implements Serializable {

        /*Declare Note class attributes*/
        String title;
        int date_modified;

        /* Note Constructor */
        public Note(.....){
            this.title = ;
            this.date_modified ;
        }

        /* Note load method */
        static public Note load(String filename) {
            Note res = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

                res = (Note) ois.readObject();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            return res;
        }

        public void save(String filename) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));

                oos.writeObject(this);
                oos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void readToVoice(){
            //
        }
}
