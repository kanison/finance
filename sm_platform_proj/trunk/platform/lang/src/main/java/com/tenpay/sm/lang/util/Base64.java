package com.tenpay.sm.lang.util;

import java.io.*;

/** Utility class to convert a string to and from base 64. */

public class Base64
{

/** The characters representing each 6-bit pattern in Base 64 */
    public static final String base64Chars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

/** Converts a string of characters into a Base 64 string. */
    public static String toBase64(String str)
    {
// Turn the original string into an array of bytes
        byte[] stringBytes = str.getBytes();

        return toBase64(stringBytes);
    }

/** Converts an array of bytes into a Base 64 string. */
    public static String toBase64(byte[] bytes)
    {
// Create a buffer to hold the output string
        StringBuffer outBuff = new StringBuffer();

// Loop through the string bytes taking 3 at a time
        for (int i=0; i < bytes.length; i += 3)
        {
// Holds 3 8-bit characters that will be converted to 4 6-bit characters
            int packed64 = 0;

// Assume that you're writing out 4 full characters each time. At the end
// of the string, you may end up padding with ='s
            int num64Chars = 4;

// Take the first char and stick it in the packing integer
            packed64 = (bytes[i] & 255) << 16;

// If there is another character in the string, add it to the buffer
            if (i+1 < bytes.length)
            {
                packed64 = packed64 + ((bytes[i+1] & 255) << 8);
            }
            else
            {
// Otherwise, you need to write out 2 pad characters. Out of 4 6-bit chars,
// you need two chars to represent an 8-bit number
                num64Chars = 2;
            }

// If there is still another character available, add it to the buffer
            if (i + 2 < bytes.length)
            {
                packed64 = packed64 + (bytes[i+2] & 255);
            }
// Otherwise, if you were still expecting to write out 4 6-bit characters,
// you really only write 3. It's possible to get to this point after already
// setting the number of characters to 2, so you need to make sure you aren't
// accidentally writing one more character than you need to
            else if (num64Chars == 4)
            {
                num64Chars = 3;
            }

// Loop through the packing integer taking 6 bits at a time. Print out the
// character in the base64Chars string that corresponds to each 6-bit
// value.
            for (int j=0; j < num64Chars; j++)
            {
                outBuff.append(base64Chars.charAt(
                    (packed64 >> (6 * (3-j))) & 63));
            }

// If you wrote less than 4 6-bit values, write out pad characters
            for (int j=num64Chars; j < 4; j++)
            {
                outBuff.append('=');
            }
        }

// Return the base64 string
        return outBuff.toString();
    }

/** Converts a base64 string to a regular string */
    public static String fromBase64(String str)
    {
        return new String(fromBase64Bytes(str));
    }

/** Converts a base64 string to an array of bytes */

    public static byte[] fromBase64Bytes(String str)
    {
// Create a buffer for converted bytes
        ByteArrayOutputStream outBuff = new ByteArrayOutputStream();

// Convert the base64 string into an array of bytes
        byte[] stringBytes = str.getBytes();

// Loop through the bytes taking 4 at a time
        for (int i=0; i < stringBytes.length; i += 4)
        {
            int currWord = 0;

// Assume that this set of bytes contains 3 whole characters
            int charsInWord = 3;

            for (int j=0; j < 4; j++)
            {
                byte ch = stringBytes[i+j];

// If you hit a pad character, you have fewer than 3 characters encoded
// in this set of 4 bytes. As it turns out, the number of characters
// encoded is equal to the position where you first encounter the '='
                if (ch == '=')
                {
                    charsInWord = j - 1;
                    break;
                }

// Get the 6-bit value that corresponds to this character
                ch = (byte) base64Chars.indexOf(ch);

// Add the 6-bits to the unpacking integer (you build a 24-bit value from
// 4 6-bit values)
                currWord = currWord + (ch << (6 * (3 - j)));
            }

// Pull the characters out of the unpacking integer by taking 8 bits at
// a time.
            for (int j=0; j < charsInWord; j++)
            {
                outBuff.write((int) ((currWord >> (8 * (2-j))) & 255));
            }
        }

// Return the unencoded bytes
        return outBuff.toByteArray();
    }

///** A test program to make sure the base64 encoding works. It encodes each
//    command-line argument into base64 and then back again.
//*/
//    public static void main(String args[]) throws FileNotFoundException,
//      IOException {
//    System.out.println(-49>>2);
//        System.out.println(-45/4);
//        for (int i=0; i < args.length; i++)
//        {
//            String base64 = toBase64(args[i]);
//            String un64 = fromBase64(base64);
//
//            System.out.println(args[i]+" -> "+base64+" -> "+un64);
//            if (!un64.equals(args[i]))
//            {
//                System.out.println("Conversion failed!");
//            }
//        }
//
//        encodeImage();
//        decodeImage();
//
//    }

    public static void decodeImage() throws FileNotFoundException, IOException {
      java.io.File f = new java.io.File("c:\\1.txt");
//      java.io.File f = new java.io.File("c:\\2.txt");
      java.io.BufferedReader br = new java.io.BufferedReader
                                  (new java.io.InputStreamReader(new java.io.FileInputStream(f)));
      String str  = br.readLine();
      System.out.println(str.length());
      br.close();
      java.io.FileOutputStream os = new java.io.FileOutputStream("c:\\2.jpg");
//      java.io.FileOutputStream os = new java.io.FileOutputStream("c:\\22.jpg");
      os.write(fromBase64Bytes(str));
      os.close();
    }

    public static void encodeImage() throws FileNotFoundException, IOException {
      java.io.File f = new java.io.File("c:\\1.jpg");
      byte[] bs = new byte[(int)f.length()];
      java.io.FileInputStream is = new java.io.FileInputStream(f);
      is.read(bs);
      is.close();

      java.io.PrintWriter pw = new java.io.PrintWriter(
      new java.io.FileOutputStream("c:\\2.txt"));
      pw.print(toBase64(bs));
      pw.close();
    }


}
