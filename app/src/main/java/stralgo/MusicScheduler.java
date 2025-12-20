package stralgo;

import java.util.*;

class Player {
    String name;
    List<String> instruments;
    Set<Integer> unavailableWeeks;

    public Player(String name, List<String> instruments, Set<Integer> unavailableWeeks) {
        this.name = name;
        this.instruments = instruments;
        this.unavailableWeeks = unavailableWeeks;
    }

    public boolean isAvailable(int week) {
        return !unavailableWeeks.contains(week);
    }

    public boolean canPlay(String instrument) {
        return instruments.contains(instrument);
    }
}

public class MusicScheduler {
    
    static String[] REQUIRED_INSTRUMENTS = {"Gitar", "Bass", "Drum", "Keyboard"};
    static List<Player> players = new ArrayList<>();
    static int TOTAL_WEEKS = 4;
    
    
    static int MAX_JOBS_PER_WEEK = 2; 

    static Map<Integer, Map<String, String>> schedule = new HashMap<>();
    static Map<String, Integer> playCounts = new HashMap<>();

    public static void main(String[] args) {
        initializeData();

        System.out.println("=== Penjadwalan Multi-Role (Max " + MAX_JOBS_PER_WEEK + " job/minggu) ===");
        
        for (Player p : players) playCounts.put(p.name, 0);

        if (solveSchedule(1, 0)) {
            printSchedule();
            printFairnessStats();
        } else {
            System.out.println("Gagal menemukan solusi! Coba tambah pemain atau longgarkan aturan.");
        }
    }

    static boolean solveSchedule(int week, int instrIdx) {
        if (week > TOTAL_WEEKS) return true;

        if (instrIdx == REQUIRED_INSTRUMENTS.length) {
            return solveSchedule(week + 1, 0);
        }

        String currentInstrument = REQUIRED_INSTRUMENTS[instrIdx];

       
        List<Player> sortedCandidates = new ArrayList<>(players);
        sortedCandidates.sort(Comparator.comparingInt(p -> playCounts.get(p.name)));

        for (Player p : sortedCandidates) {
            if (isValid(p, week, currentInstrument)) {
                
               
                schedule.putIfAbsent(week, new HashMap<>());
                schedule.get(week).put(currentInstrument, p.name);
                
                
                playCounts.put(p.name, playCounts.get(p.name) + 1);

                
                if (solveSchedule(week, instrIdx + 1)) {
                    return true;
                }

                
                schedule.get(week).remove(currentInstrument);
                playCounts.put(p.name, playCounts.get(p.name) - 1);
            }
        }

        return false;
    }

  
    static boolean isValid(Player p, int week, String instrument) {
        
        if (!p.canPlay(instrument)) return false;
        
        
        if (!p.isAvailable(week)) return false;

        
        Map<String, String> weekSchedule = schedule.get(week);
        if (weekSchedule != null) {
            int currentJobs = 0;
            for (String assignedPlayer : weekSchedule.values()) {
                if (assignedPlayer.equals(p.name)) {
                    currentJobs++;
                }
            }
            
            if (currentJobs >= MAX_JOBS_PER_WEEK) {
                return false;
            }
            
            
        }

        return true;
    }

    static void initializeData() {
        players.add(new Player("A", Arrays.asList("Gitar", "Bass"), new HashSet<>(Arrays.asList(1,2))));
        players.add(new Player("B", Arrays.asList("Gitar", "Bass"), new HashSet<>(Arrays.asList(1,2))));
        players.add(new Player("C", Arrays.asList("Keyboard"), new HashSet<>(Arrays.asList(3,4))));
        players.add(new Player("D", Arrays.asList("Keyboard"), new HashSet<>()));
        players.add(new Player("E", Arrays.asList("Drum"), new HashSet<>()));
        players.add(new Player("F", Arrays.asList("Drum"), new HashSet<>()));
        players.add(new Player("G", Arrays.asList("Gitar"), new HashSet<>()));
        players.add(new Player("H", Arrays.asList("Bass"), new HashSet<>()));
        players.add(new Player("I", Arrays.asList("Bass"), new HashSet<>()));
    }

    static void printSchedule() {
        System.out.println("\n=== HASIL JADWAL ===");
        for (int w = 1; w <= TOTAL_WEEKS; w++) {
            System.out.println("Minggu ke-" + w + ":");
            Map<String, String> weekSch = schedule.get(w);
            
            Map<String, Integer> jobCount = new HashMap<>();
            
            for (String instr : REQUIRED_INSTRUMENTS) {
                String name = weekSch.get(instr);
                System.out.printf("  %-10s : %s%n", instr, name);
                jobCount.put(name, jobCount.getOrDefault(name, 0) + 1);
            }
            
            System.out.print("  [Info] Pemain Rangkap: ");
            boolean ada = false;
            for(Map.Entry<String, Integer> e : jobCount.entrySet()){
                if(e.getValue() > 1) {
                    System.out.print(e.getKey() + "(" + e.getValue() + " posisi) ");
                    ada = true;
                }
            }
            if(!ada) System.out.print("-");
            System.out.println("\n");
        }
    }

    static void printFairnessStats() {
        System.out.println("=== TOTAL MAIN (Fairness Check) ===");
        for (Map.Entry<String, Integer> entry : playCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
