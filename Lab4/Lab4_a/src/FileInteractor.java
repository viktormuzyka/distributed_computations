import java.io.*;
import java.util.ArrayList;

public class FileInteractor {
    private String fileName;
    private ObjectOutputStream output;
    private ReadWriteLock lock;

    public FileInteractor(String fileName) {
        this.fileName = fileName;
        this.lock = new ReadWriteLock();
        try {
            this.output = new ObjectOutputStream(new FileOutputStream(fileName, true));
            clearFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public enum Field {
        PHONE,
        NAME
    }

    public void writeToFile(Writer writer) {
        try {
            lock.lockWrite();
            output.writeObject(writer);
            lock.unlockWrite();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Writer findInFile(String key, Field field) {
        try {
            lock.lockRead();
            FileInputStream istream = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(istream);

            boolean isFound = false;
            Writer answer = new Writer("", "");
            while (!isFound && istream.available() > 0) {
                Writer writer = (Writer) input.readObject();
                switch (field) {
                    case NAME -> {
                        if (writer.getName().equals(key)) {
                            answer = writer;
                            isFound = true;
                        }
                    }
                    case PHONE -> {
                        if (writer.getPhone().equals(key)) {
                            answer = writer;
                            isFound = true;
                        }
                    }
                }
            }
            lock.unlockRead();
            return answer;
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearFile() throws IOException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
        output = new ObjectOutputStream(new FileOutputStream(fileName, true));
    }

    private ArrayList<Writer> getAllItems(FileInputStream istream, ObjectInputStream input) throws IOException, ClassNotFoundException {
        ArrayList<Writer> array = new ArrayList<>();
        while (istream.available() > 0) {
            Writer buffer = (Writer) input.readObject();
            array.add(buffer);
        }
        return array;
    }


    public void removeByKey(String key, Field field) {
        try{
            lock.lockWrite();
            FileInputStream istream = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(istream);

            ArrayList<Writer> array = getAllItems(istream, input);

            int index = -1;
            switch (field){
                case NAME -> {
                    for (int i = 0; i < array.size(); ++i) {
                        if (array.get(i).getName().equals(key)) {
                            index = i;
                            break;
                        }
                    }
                }
                case PHONE -> {
                    for (int i = 0; i < array.size(); ++i) {
                        if (array.get(i).getPhone().equals(key)) {
                            index = i;
                            break;
                        }
                    }
                }
            }

            if(index == -1){
                lock.unlockWrite();
                return;
            }
            array.remove(index);

            clearFile();

            for (Writer item : array) {
                output.writeObject(item);
            }
            lock.unlockWrite();
        } catch (IOException | InterruptedException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void readFile() throws IOException, ClassNotFoundException, InterruptedException {
        lock.lockRead();
        FileInputStream istream = new FileInputStream(fileName);
        ObjectInputStream input = new ObjectInputStream(istream);

        while (istream.available() > 0) {
            Writer buffer = (Writer) input.readObject();
            System.out.println(buffer);
        }
        lock.unlockRead();
    }
}