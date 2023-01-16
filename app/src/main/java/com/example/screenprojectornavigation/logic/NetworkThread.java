package com.example.screenprojectornavigation.logic;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class NetworkThread extends Thread {

    private String payload;
    private static final int RED = -65536;
    private NetworkThread() {
    }
    private NetworkThread(String payload) {
        this.payload = payload;
    }

    public static NetworkThread createUDPSenderForString(String message) {
        return new NetworkThread(message);
    }

    public static NetworkThread createUDPSenderForBitmap(Bitmap bmp) {
        System.out.println("sending started...");
        int size = bmp.getRowBytes()*bmp.getHeight();
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        int[][] bitmapMatrix = new int[bmp.getHeight()][bmp.getWidth()];
        int rows=0;
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for(int i = 0; i< bmp.getHeight(); i += 1) {
            for(int j = 0; j<bmp.getWidth(); j += 1) {
                int index = i*bmp.getWidth()+j;
                if(pixels[index] == RED) {
                    bitmapMatrix[i][j] = 0;
                }
                else {
                    bitmapMatrix[i][j] = 1;
                }

            }
        }
        int[][] returnVal = rescalePicture(bitmapMatrix);
        String sendMessage = "";
        for (int i =0;i<32;i+=1) {
            System.out.println(i+" "+returnVal[i][0]+" "+
                    returnVal[i][1]+" "+returnVal[i][2]+" "+returnVal[i][3]+" "+returnVal[i][4]+" "+
                    returnVal[i][5]+" "+returnVal[i][6]+" "+
                    returnVal[i][7]);
            sendMessage += String.valueOf(returnVal[i][0])+String.valueOf(returnVal[i][1])+String.valueOf(returnVal[i][2])+String.valueOf(returnVal[i][3])+String.valueOf(returnVal[i][4])+String.valueOf(returnVal[i][5])+String.valueOf(returnVal[i][6])+String.valueOf(returnVal[i][7]);

        }
        System.out.println("making message finished!"+bmp.getWidth()+" "+bmp.getHeight());
        return new NetworkThread(sendMessage);
    }

    private static int[][] rescalePicture(int[][]pixels) {
        int[][] returnVal = new int[32][8];
        int[][] reduceColumns = new int[32*20][8];
        for(int i = 0; i<=20*32; i += 1) {
            if(i%20==0 && i!=0) {
                System.out.println("multiply of 20: "+i);
                for(int z = 0; z<8; z += 1) {
                    int temp = reduceColumns[i-20][z]+reduceColumns[i-19][z]+reduceColumns[i-18][z]+
                            reduceColumns[i-17][z]+reduceColumns[i-16][z]+reduceColumns[i-15][z]+
                            reduceColumns[i-14][z]+reduceColumns[i-13][z]+reduceColumns[i-12][z]+
                            reduceColumns[i-11][z]+reduceColumns[i-10][z]+reduceColumns[i-9][z]+
                            reduceColumns[i-8][z]+reduceColumns[i-7][z]+reduceColumns[i-6][z]+
                            reduceColumns[i-5][z]+reduceColumns[i-4][z]+reduceColumns[i-3][z]+
                            reduceColumns[i-2][z]+reduceColumns[i-1][z];
                    if(temp>=200)
                        returnVal[i/20-1][z] = 0;
                    else
                        returnVal[i/20-1][z] = 1;
                }
                if(i==20*32)
                    return returnVal;
            }
            for (int j = 0; j<8; j += 1) {
                int count=0;
                for(int k = 0; k<20; k += 1) {
                    if(pixels.length > i && pixels[0].length > pixels[i].length && pixels[i][j*20+k] == 0) {
                        count+=1;
                    }
                    else
                        count = count;
                }
                reduceColumns[i][j] = count;
            }
        }
        return returnVal;
    }


    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();


            System.out.println(payload);
            String testText = payload;
            byte[] bytes = testText.getBytes();
            System.out.println("bytes: " + Arrays.deepToString(new byte[][]{bytes}));
            byte[] ipAddr = new byte[]{(byte) XXX, (byte) XXX, XXX, XXX};
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                    InetAddress.getByAddress(ipAddr), 8000);
            try {
                datagramSocket.send(datagramPacket);

            } catch (Exception e) {
                System.out.println("execpt");
                System.out.println("e" + e);
            }
            System.out.println("sending ended...");

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

}
