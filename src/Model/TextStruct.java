package Model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//
// 1
// 00:00:00,500 --> 00:00:02,207
// Once upon a time ...
// [newline]
// 2 
//
public class TextStruct {
    private static SimpleDateFormat mSdf = new SimpleDateFormat("hh:mm:ss,SSS"); 
    private ArrayList<String> mTextLines = null;
    private int  mDiffSeconds= 0;
    private Date mFromTime   = null;
    private Date mToTime     = null;

    public TextStruct(ArrayList<String> textLines){
      mTextLines=textLines;
      String[] splited = mTextLines.get(1).split(" ");
      try {
       mFromTime=mSdf.parse(splited[0]);
       mToTime=mSdf.parse(splited[2]);
      } catch( Exception e){
       e.printStackTrace();
      }
    }

    public String addSeconds(int aDiffSeconds){
      mDiffSeconds = aDiffSeconds;
      String newline=System.getProperty("line.separator");
      String s="";
      for (int i=0;i< mTextLines.size(); i++ ){
        if (i==1) {
          s+=mSdf.format(addTime(mFromTime)) + " --> " + mSdf.format(addTime(mToTime)) +newline;
        } else {
   s+=mTextLines.get(i) + newline;
        }
      }
      return s;
    }
    
    private Date addTime(Date d){
      Calendar cal=Calendar.getInstance();
      cal.setTime(d);
      cal.add(Calendar.SECOND, mDiffSeconds);
      return cal.getTime();
    }
  }