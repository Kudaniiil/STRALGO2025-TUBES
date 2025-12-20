package stralgo;

import java.util.*;

class StrictPlayer {
    String name;
    List<String> instruments;
    Set<Integer> availableWeeks;

    public StrictPlayer(String name, List<String> instruments, List<Integer> availableWeeks) {
        this.name = name;
        this.instruments = instruments;
        this.availableWeeks = new HashSet<>(availableWeeks);
    }

    public boolean canPlay(String instr) {
        return instruments.contains(instr);
    }

    public boolean isAvailable(int week) {
        return availableWeeks.contains(week);
    }
}

public class MusicSchedulerStrict {

    static String[] REQUIRED_INSTRUMENTS = {"Gitar", "Bass", "Keyboard", "Drum"};
    
    static List<StrictPlayer> players = new ArrayList<>();
    static int TOTAL_WEEKS = 4;

    static Map<Integer, Map<String, String>> schedule = new HashMap<>();
    
    static Map<Integer, Set<String>> assignedPlayersPerWeek = new HashMap<>();

    public static void main(String[] args) {
        loadTestCase(1); 

        System.out.println("=== Penjadwalan Strict (1 Orang = 1 Alat) ===");
        
        if (solveSchedule(1, 0)) {
            printSchedule();
        } else {
            System.out.println("Solusi tidak ditemukan! Cek ketersediaan pemain.");
        }
    }

    static boolean solveSchedule(int week, int instrIdx) {
        if (week > TOTAL_WEEKS) {
            return true;
        }

        if (instrIdx == REQUIRED_INSTRUMENTS.length) {
            return solveSchedule(week + 1, 0);
        }

        String currentInstrument = REQUIRED_INSTRUMENTS[instrIdx];

        for (StrictPlayer p : players) {
            if (isValid(p, week, currentInstrument)) {
                
                schedule.putIfAbsent(week, new HashMap<>());
                schedule.get(week).put(currentInstrument, p.name);
                
                assignedPlayersPerWeek.putIfAbsent(week, new HashSet<>());
                assignedPlayersPerWeek.get(week).add(p.name);

                if (solveSchedule(week, instrIdx + 1)) {
                    return true;
                }

                schedule.get(week).remove(currentInstrument);
                assignedPlayersPerWeek.get(week).remove(p.name);
            }
        }

        return false;
    }

    static boolean isValid(StrictPlayer p, int week, String instrument) {
        if (!p.canPlay(instrument)) return false;

        if (!p.isAvailable(week)) return false;

        if (assignedPlayersPerWeek.containsKey(week) && 
            assignedPlayersPerWeek.get(week).contains(p.name)) {
            return false;
        }

        return true;
    }

    static void loadTestCase(int caseNum) {
        players.clear();
        
        if (caseNum == 1) {
            System.out.println("Loading Data Test Case 1...");
            TOTAL_WEEKS = 4;
            
            players.add(new StrictPlayer("A", Arrays.asList("Gitar", "Bass"), Arrays.asList(3, 4))); 
            players.add(new StrictPlayer("B", Arrays.asList("Gitar", "Bass"), Arrays.asList(3, 4)));
            
            players.add(new StrictPlayer("C", Arrays.asList("Keyboard"), Arrays.asList())); 
            
            players.add(new StrictPlayer("D", Arrays.asList("Keyboard"), Arrays.asList(1, 2, 3, 4)));
            players.add(new StrictPlayer("E", Arrays.asList("Drum"), Arrays.asList(1, 2, 3, 4)));
            players.add(new StrictPlayer("F", Arrays.asList("Drum"), Arrays.asList(1, 2, 3, 4)));
            players.add(new StrictPlayer("G", Arrays.asList("Gitar"), Arrays.asList(1, 2, 3, 4)));
            players.add(new StrictPlayer("H", Arrays.asList("Bass"), Arrays.asList(1, 2, 3, 4)));
            players.add(new StrictPlayer("I", Arrays.asList("Gitar"), Arrays.asList(1, 2, 3, 4)));
            
        } else if (caseNum == 2) {
            System.out.println("Loading Data Test Case 2...");
            TOTAL_WEEKS = 2;
            
            players.add(new StrictPlayer("C", Arrays.asList("Keyboard"), Arrays.asList(2)));
            players.add(new StrictPlayer("D", Arrays.asList("Drum"), Arrays.asList(2)));
            players.add(new StrictPlayer("F", Arrays.asList("Gitar"), Arrays.asList(1)));
            players.add(new StrictPlayer("G", Arrays.asList("Keyboard"), Arrays.asList(1)));
            players.add(new StrictPlayer("H", Arrays.asList("Drum"), Arrays.asList(1)));
            players.add(new StrictPlayer("Elias", Arrays.asList("Bass"), Arrays.asList(1, 2)));

        } else if (caseNum == 3) {
            System.out.println("Loading Data Test Case 3...");
            TOTAL_WEEKS = 2;
            
            players.add(new StrictPlayer("A", Arrays.asList("Gitar", "Bass"), Arrays.asList(2))); 
            players.add(new StrictPlayer("B", Arrays.asList("Gitar", "Bass"), Arrays.asList(2)));
            
            players.add(new StrictPlayer("C", Arrays.asList("Keyboard"), Arrays.asList(2))); 
            players.add(new StrictPlayer("D", Arrays.asList("Keyboard"), Arrays.asList(1, 2)));
            players.add(new StrictPlayer("E", Arrays.asList("Drum"), Arrays.asList(1)));
            players.add(new StrictPlayer("F", Arrays.asList("Drum"), Arrays.asList(1)));
            players.add(new StrictPlayer("G", Arrays.asList("Gitar"), Arrays.asList(1)));
            players.add(new StrictPlayer("H", Arrays.asList("Bass"), Arrays.asList(1)));
            players.add(new StrictPlayer("I", Arrays.asList("Gitar"), Arrays.asList(1)));
        }
    }

    static void printSchedule() {
        System.out.println("\n=== HASIL JADWAL (1 Orang = 1 Alat) ===");
        for (int w = 1; w <= TOTAL_WEEKS; w++) {
            System.out.println("Minggu " + w + ":");
            Map<String, String> weekSch = schedule.get(w);
            if (weekSch == null || weekSch.isEmpty()) {
                System.out.println("  Libur (Tidak ada jadwal)");
                continue;
            }
            for (String instr : REQUIRED_INSTRUMENTS) {
                String player = weekSch.get(instr);
                System.out.printf("  %-10s : %s%n", instr, (player != null ? player : "-"));
            }
            System.out.println();
        }
    }

    
}