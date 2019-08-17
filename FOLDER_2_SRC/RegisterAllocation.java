import TEMP.TEMP_FACTORY;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterAllocation {

    public static void allocate(String outputPath) throws IOException {
        String inputFile = "FOLDER_5_OUTPUT/MIPS.txt";
//        String inputFile = "/home/vladko/Downloads/COMPILATION/EX4/FOLDER_5_OUTPUT/MIPS.txt";
        List<TempMetadata> tempList = setStartAndEndPointForEachTemp(inputFile);
        Graph g = new Graph(tempList.size());
        setIntersections(tempList, g);
        replaceTempRegisters(outputPath, inputFile, g.greedyColoring());
    }

    private static void replaceTempRegisters(String outputPath, String inputFile, HashMap<Integer, Integer> coloringResults) throws IOException {
        List<String> outputCommandsList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            for (int i = TEMP_FACTORY.getTempNumber(); i >= 0; i--) {
                Matcher m = Pattern.compile("Temp_" + String.valueOf(i)).matcher(line);
                line = m.replaceAll("\\$t" + coloringResults.get(i));
            }
            outputCommandsList.add(line);
        }
        Files.write(Paths.get(outputPath), outputCommandsList);
    }

    private static void setIntersections(List<TempMetadata> tempList, Graph g) {
        for (TempMetadata t1 : tempList) {
            for (TempMetadata t2 : tempList) {
                if (t2.isInter(t1) && !t2.equals(t1)) {
                    t2.interSet.add(t1.tempIdx);
                    g.addEdge(t1.tempIdx, t2.tempIdx);
                }
            }
        }
        tempList.sort(Comparator.comparingInt(tempMetadata -> tempMetadata.interSet.size()));
    }

    private static List<TempMetadata> setStartAndEndPointForEachTemp(String inputFile) throws IOException {
        Pattern p = Pattern.compile("Temp_(\\d+)");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        int lineNum = 1;
        String line;
        HashMap<Integer, TempMetadata> tempsRanges = new HashMap<>();
        while ((line = bufferedReader.readLine()) != null) {
            Matcher m = p.matcher(line);
            while (m.find()) {
                int tmp_idx = Integer.parseInt(m.group(1));
                if(tempsRanges.containsKey(tmp_idx)){
                    tempsRanges.get(tmp_idx).end = lineNum;
                }else{
                    tempsRanges.put(tmp_idx,new TempMetadata(tmp_idx,lineNum,lineNum));
                }
            }
            lineNum++;
        }
        List<TempMetadata> tempList = new ArrayList<>(tempsRanges.values());
        tempList.sort((TempMetadata a, TempMetadata b) -> a.tempIdx -b.tempIdx);
        return tempList;
    }
}
