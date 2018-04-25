package com.company;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
//        Path dataPath = FileSystems.getDefault().getPath("Data.txt");
//        Files.write(dataPath, "\nLine 4".getBytes("UTF-8"), StandardOpenOption.APPEND);
//        List<String> lines = Files.readAllLines(dataPath);
//        for(String line: lines){
//
//            System.out.println(line);
//        }

        //  Pipe is used to send data from one Thread to another. It is a one way connection.
        //  Pipe got 2 channels. Sink Channel and Source Channel.
        try {
            Pipe pipe = Pipe.open();
            Runnable writer = new Runnable() {
                @Override
                public void run() {
                    try {
                        Pipe.SinkChannel sinkChannel = pipe.sink();
                        ByteBuffer buffer = ByteBuffer.allocate(56);

                        for (int i = 0; i < 10; i++) {
                            String currentTime = "The time is: " + System.currentTimeMillis();

                            buffer.put(currentTime.getBytes());
                            buffer.flip();

                            while (buffer.hasRemaining()) {
                                sinkChannel.write(buffer);
                            }
                            buffer.flip();
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Runnable reader = new Runnable() {
                @Override
                public void run() {
                    try{
                        Pipe.SourceChannel sourceChannel = pipe.source();
                        ByteBuffer buffer = ByteBuffer.allocate(56);

                        for (int i=0; i<10; i++){
                            int bytesRead = sourceChannel.read(buffer);
                            byte[] timeString = new byte[bytesRead];

                            buffer.flip();
                            buffer.get(timeString);
                            System.out.println("Reader Thread: " + new String(timeString));

                            buffer.flip();
                            Thread.sleep(100);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            new Thread(writer).start();
            new Thread(reader).start();
        } catch (IOException e){
            e.printStackTrace();
        }


        //  Writing in Data.dat file

//        try(FileOutputStream binfile = new FileOutputStream("Data.dat");
//            FileChannel binChannel = binfile.getChannel()){
//
////            ByteBuffer buffer = ByteBuffer.allocate(100);
////            byte[] outputByte = "Hello World!".getBytes();
////            byte[] outputByte2 = "Working on JAVA".getBytes();
////            buffer.put(outputByte).putInt(245).putInt(-98765).put(outputByte2).putInt(1000);
////            buffer.flip();
//
//            ByteBuffer buffer = ByteBuffer.allocate(100);
//            byte[] outputByte = "Hello World!".getBytes();
//            buffer.put(outputByte);
//            long intPos1 = outputByte.length;
//            buffer.putInt(245);
//            long intPos2 = intPos1 + Integer.BYTES;
//            buffer.putInt(-98765);
//
//            byte[] outputByte2 = "Working on Java".getBytes();
//            buffer.put(outputByte2);
//            long intPos3 = intPos2 + Integer.BYTES + outputByte2.length;
//            buffer.putInt(1000);
//            buffer.flip();
//
//            binChannel.write(buffer);
//
////              Reading from Data.dat file
//            RandomAccessFile ra = new RandomAccessFile("Data.dat","rwd");
//            FileChannel channel = ra.getChannel();
//
//            ByteBuffer readBuffer = ByteBuffer.allocate(Integer.BYTES);
//            channel.position(intPos3);
//            channel.read(readBuffer);
//            readBuffer.flip();
//            System.out.println("Int 3 : " + readBuffer.getInt());
//
//            readBuffer.flip();
//            channel.position(intPos2);
//            channel.read(readBuffer);
//            readBuffer.flip();
//            System.out.println("Int 2 : " + readBuffer.getInt());
//
//            readBuffer.flip();
//            channel.position(intPos1);
//            channel.read(readBuffer);
//            readBuffer.flip();
//            System.out.println("Int 1 : " + readBuffer.getInt());
//
//            // Creating copy of a file.
//
//            RandomAccessFile ra1 = new RandomAccessFile("DataCopy.dat","rw");
//            FileChannel copyChannel = ra1.getChannel();
//
//            channel.position(0);
////            long numTransferred = copyChannel.transferFrom(channel,0,channel.size());
//            long numTransferred = channel.transferTo(0,channel.size(),copyChannel);
//            System.out.println(numTransferred + " num transferred.");
//
////            byte[] outputString1 = "Hello World".getBytes();
////            long strPos1 = 0;
////            long newInt1Pos = outputString1.length;
////            long newInt2Pos = newInt1Pos + Integer.BYTES;
////            byte[] outputString2 = "Working on Java".getBytes();
////            long strPos2 = newInt2Pos + Integer.BYTES;
////            long newInt3Pos = strPos2 + outputString2.length;
////
////            ByteBuffer intBuffer = ByteBuffer.allocate(Integer.BYTES);
////            intBuffer.putInt(245);
////            intBuffer.flip();
////            binChannel.position(newInt1Pos);
////            binChannel.write(intBuffer);
////
////            intBuffer.flip();
////            intBuffer.putInt(-9765);
////            intBuffer.flip();
////            binChannel.position(newInt2Pos);
////            binChannel.write(intBuffer);
////
////            intBuffer.flip();
////            intBuffer.putInt(1000);
////            intBuffer.flip();
////            binChannel.position(newInt3Pos);
////            binChannel.write(intBuffer);
////
////            binChannel.position(strPos1);
////            binChannel.write(ByteBuffer.wrap(outputString1));
////            binChannel.position(strPos2);
////            binChannel.write(ByteBuffer.wrap(outputString2));
//
////            ByteBuffer readBuffer = ByteBuffer.allocate(100);
////            channel.read(readBuffer);
////            readBuffer.flip();
////            byte[] input1 = new byte[outputByte.length];
////            readBuffer.get(input1);
////            System.out.println("Input String: " + new String(input1));
////            System.out.println("List 1: " + readBuffer.getInt());
////            System.out.println("List 2: " + readBuffer.getInt());
////            byte[] input2 = new byte[outputByte2.length];
////            readBuffer.get(input2);
////            System.out.println("Input String: " + new String(input2));
////            System.out.println("List 3: " + readBuffer.getInt());
//
//            //  Writing in Data.dat file
//
////            try(FileOutputStream binfile = new FileOutputStream("Data.dat");
////                FileChannel binChannel = binfile.getChannel()){
////            byte[] outputByte = "Hello World!".getBytes();
//////            ByteBuffer buffer = ByteBuffer.wrap(outputByte);       //     I can use below listed 3 lines or only this one.
////            ByteBuffer buffer = ByteBuffer.allocate(outputByte.length);
////            buffer.put(outputByte);
////            buffer.flip();
////
////            int numBytes = binChannel.write(buffer);
////            System.out.println("Number of Bytes written is " + numBytes);
////
////            ByteBuffer  intBuffer = ByteBuffer.allocate(Integer.BYTES);
////            intBuffer.putInt(245);
////            intBuffer.flip();
////            numBytes = binChannel.write(intBuffer);
////            System.out.println("NUmber uf Bytes written are " + numBytes);
////
////            intBuffer.flip();
////            intBuffer.putInt(-98765);
////            intBuffer.flip();
////            numBytes = binChannel.write(intBuffer);
////            System.out.println("Number of Bytes written are " + numBytes);
////
////            //  Reading file using IO library.
////
//////            RandomAccessFile ra = new RandomAccessFile("Data.dat","rwd");
//////            byte[] b = new byte[outputByte.length];
//////            ra.read(b);
//////
//////            System.out.println(new String(b));
//////            long int1 = ra.readInt();
//////            long int2 = ra.readInt();
//////            System.out.println(int1);
//////            System.out.println(int2);
////
////            //  Relative Read
////
//////            RandomAccessFile ra = new RandomAccessFile("Data.dat","rwd");
//////            FileChannel channel = ra.getChannel();
//////            buffer.flip();
//////            long numBytesRead = channel.read(buffer);
//////            if(buffer.hasArray()){
//////                System.out.println("byte Buffer = " + new String(buffer.array()));
//////            }
//////            intBuffer.flip();
//////            channel.read(intBuffer);
//////            intBuffer.flip();
//////            System.out.println(intBuffer.getInt());
//////            intBuffer.flip();
//////            channel.read(intBuffer);
//////            intBuffer.flip();
//////            System.out.println(intBuffer.getInt());
//////            channel.close();
//////            ra.close();
////
////            //  Absolute Read - Doesn't Change Buffer Position
////
////            RandomAccessFile ra = new RandomAccessFile("Data.dat","rwd");
////            FileChannel channel = ra.getChannel();
////            buffer.flip();
////            channel.read(buffer);
////            if(buffer.hasArray()){
////                System.out.println("byte Buffer = " + new String(buffer.array()));
////            }
////            intBuffer.flip();
////            channel.read(intBuffer);
////            System.out.println(intBuffer.getInt(0));
////            intBuffer.flip();
////            channel.read(intBuffer);
////            System.out.println(intBuffer.getInt(0));
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }
}
