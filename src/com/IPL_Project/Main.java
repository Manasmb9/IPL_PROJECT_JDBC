package com.IPL_Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static final int MATCH_ID = 1;
    public static final int INNINGS = 2;
    public static final int BATTING_TEAM = 3;
    public static final int BOWLING_TEAM = 4;
    public static final int OVER = 5;
    public static final int BALL = 6;
    public static final int BATSMAN = 7;
    public static final int NON_STRIKER = 8;
    public static final int BOWLER = 9;
    public static final int IS_SUPER_OVER = 10;
    public static final int WIDE_RUNS = 11;
    public static final int BYE_RUNS = 12;
    public static final int LEG_BYE_RUNS = 13;
    public static final int NO_BALL_RUNS = 14;
    public static final int PENALTY_RUNS = 15;
    public static final int BATSMAN_RUNS = 16;
    public static final int EXTRA_RUNS = 17;
    public static final int TOTAL_RUNS = 18;
    public static final int ID = 1;
    public static final int SEASON = 2;
    public static final int CITY = 3;
    public static final int DATE = 4;
    public static final int TEAM_1 = 5;
    public static final int TEAM_2 = 6;
    public static final int TOSS_WINNER = 7;
    public static final int TOSS_DECISION = 8;
    public static final int RESULT = 9;
    public static final int DL_APPLIED = 10;
    public static final int WINNER = 11;
    public static final int WIN_BY_RUNS = 12;
    public static final int WIN_BY_WICKETS = 13;

    public static ArrayList<Match> getMatchesData(Connection con) throws SQLException {

        ResultSet data;
        String query = "SELECT * FROM matches";
        Statement st = con.createStatement();
        data = st.executeQuery(query);
        ArrayList<Match> matchData = new ArrayList<>();
        while (data.next()) {
            Match match = new Match();
            match.setId(data.getInt((ID)));
            match.setSeason(data.getInt(SEASON));
            match.setCity(data.getString(CITY));
            match.setDate(data.getString(DATE));
            match.setTeam1(data.getString(TEAM_1));
            match.setTeam2(data.getString(TEAM_2));
            match.setTossWinner(data.getString(TOSS_WINNER));
            match.setTossDecision(data.getString(TOSS_DECISION));
            match.setResult(data.getString(RESULT));
            match.setDl_applied(data.getInt(DL_APPLIED));
            match.setWinner(data.getString(WINNER));
            match.setWin_by_runs(data.getInt(WIN_BY_RUNS));
            match.setWin_by_wickets(data.getInt(WIN_BY_WICKETS));
            matchData.add(match);
        }
        return matchData;
    }

    public static ArrayList<Delivery> getDeliveryData(Connection con) throws SQLException {
        ResultSet data;
        Statement st = con.createStatement();
        String query = "SELECT * FROM Delivery";
        data = st.executeQuery(query);
        ArrayList<Delivery> deliveryData = new ArrayList<>();
        while (data.next()) {
            Delivery delivery = new Delivery();
            delivery.setMatchId(data.getInt(MATCH_ID));
            delivery.setInnings(data.getInt(INNINGS));
            delivery.setBattingTeam(data.getString(BATTING_TEAM));
            delivery.setBowlingTeam(data.getString(BOWLING_TEAM));
            delivery.setOver(data.getInt(OVER));
            delivery.setBall(data.getInt(BALL));
            delivery.setBatsman(data.getString(BATSMAN));
            delivery.setNon_Striker((data.getString(NON_STRIKER)));
            delivery.setBowler(data.getString(BOWLER));
            delivery.setIssuperover(data.getInt(IS_SUPER_OVER));
            delivery.setWideRuns(data.getInt(WIDE_RUNS));
            delivery.setByeRuns(data.getInt(BYE_RUNS));
            delivery.setLegByeRuns(data.getInt(LEG_BYE_RUNS));
            delivery.setNoballRuns(data.getInt(NO_BALL_RUNS));
            delivery.setPenaltyRuns(data.getInt(PENALTY_RUNS));
            delivery.setBatsmanRuns(data.getInt(BATSMAN_RUNS));
            delivery.setExtraRuns(data.getInt(EXTRA_RUNS));
            delivery.setTotalRuns(data.getInt(TOTAL_RUNS));
            deliveryData.add(delivery);
        }
        return deliveryData;
    }

    public static void main(String[] args) throws SQLException {
        Connection con;
        String databaseName = "test";
        String username = "postgres";
        String password = "Test@123";
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName, username, password);
            ArrayList<Match> matchTotalData = getMatchesData(con);
            ArrayList<Delivery> deliveriesTotalData = getDeliveryData(con);
            findMatchesPlayedPerYear(matchTotalData);
            findMatchesWonPerTeam(matchTotalData);
            findExtraRunsConcededPerTeam2016(matchTotalData, deliveriesTotalData);
            findEconomicBowler2015(matchTotalData, deliveriesTotalData);
            findTossWinnersPerTeam(matchTotalData);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void findTossWinnersPerTeam(ArrayList<Match> matchTotalData) {
        HashMap<String, Integer> tossWinnersPerTeam = new HashMap<>();
        for (Match match : matchTotalData) {
            if (tossWinnersPerTeam.containsKey(match.getTossWinner())) {
                tossWinnersPerTeam.put(match.getTossWinner(), tossWinnersPerTeam.get(match.getTossWinner()) + 1);
            } else {
                tossWinnersPerTeam.put(match.getTossWinner(), 1);
            }
        }
        System.out.println(tossWinnersPerTeam);
    }

    private static void findEconomicBowler2015(ArrayList<Match> matchTotalData, ArrayList<Delivery> deliverTotalData) {
        HashMap<String, Integer> bowlsCount = new HashMap<>();
        HashMap<String, Integer> bowlerRuns = new HashMap<>();
        String economicBowler = "";
        int firstMatchId = 0;
        int lastMatchId = 0;
        for (Match match : matchTotalData) {
            if (match.getSeason() == 2015) {
                if (firstMatchId == 0) {
                    firstMatchId = match.getId();
                }
                lastMatchId = match.getId();
            }
        }
        for (Delivery delivery : deliverTotalData) {
            if (delivery.getMatchId() >= firstMatchId && delivery.getMatchId() <= lastMatchId) {
                if (bowlsCount.containsKey(delivery.getBowler())) {
                    bowlsCount.put(delivery.getBowler(), bowlsCount.get(delivery.getBowler()) + 1);
                } else {
                    bowlsCount.put(delivery.getBowler(), 1);
                }
                if (bowlerRuns.containsKey(delivery.getBowler())) {
                    bowlerRuns.put(delivery.getBowler(), bowlerRuns.get(delivery.getBowler()) + delivery.getTotalRuns());
                } else {
                    bowlerRuns.put(delivery.getBowler(), delivery.getTotalRuns());
                }
            }
        }
        float economicBowlerValue, bestEconomicBowlerValue = Float.MAX_VALUE;
        for (String bowler : bowlerRuns.keySet()) {
            economicBowlerValue = (bowlerRuns.get(bowler) * 6F / bowlsCount.get(bowler));
            if (economicBowlerValue < bestEconomicBowlerValue) {
                bestEconomicBowlerValue = economicBowlerValue;
                economicBowler = bowler;
            }
        }
        System.out.println(economicBowler);
    }

    private static void findExtraRunsConcededPerTeam2016(ArrayList<Match> matchTotalData, ArrayList<Delivery> deliverTotalData) {
        HashMap<String, Integer> extraRunsConcededPerTeam = new HashMap<>();
        int firstMatchId = 0;
        int lastMatchId = 0;
        for (Match match : matchTotalData) {
            if (match.getSeason() == 2016) {
                if (firstMatchId == 0) {
                    firstMatchId = match.getId();
                }
                lastMatchId = match.getId();
            }
        }
        for (Delivery delivery : deliverTotalData) {
            if (delivery.getMatchId() >= firstMatchId && delivery.getMatchId() <= lastMatchId) {
                if (extraRunsConcededPerTeam.containsKey(delivery.getBattingTeam())) {
                    extraRunsConcededPerTeam.put(delivery.getBattingTeam(), extraRunsConcededPerTeam.get(delivery.getBattingTeam()) + delivery.getExtraRuns());
                } else {
                    extraRunsConcededPerTeam.put(delivery.getBattingTeam(), delivery.getExtraRuns());
                }
            }
        }
        System.out.println(extraRunsConcededPerTeam);
    }

    private static void findMatchesWonPerTeam(ArrayList<Match> matchTotalData) {
        HashMap<String, Integer> matchesWonPerTeam = new HashMap<>();
        for (Match match : matchTotalData) {
            if (!match.getResult().equals("no result")) {
                if (matchesWonPerTeam.containsKey(match.getWinner())) {
                    matchesWonPerTeam.put(match.getWinner(), matchesWonPerTeam.get(match.getWinner()) + 1);
                } else {
                    matchesWonPerTeam.put(match.getWinner(), 1);
                }
            }
        }
        System.out.println(matchesWonPerTeam);
    }

    private static void findMatchesPlayedPerYear(ArrayList<Match> matchTotalData) {
        HashMap<Integer, Integer> matchesPlayedPerYear = new HashMap<>();
        for (Match match : matchTotalData) {
            if (matchesPlayedPerYear.containsKey(match.getSeason())) {
                matchesPlayedPerYear.put(match.getSeason(), matchesPlayedPerYear.get(match.getSeason()) + 1);
            } else {
                matchesPlayedPerYear.put(match.getSeason(), 1);
            }
        }
        System.out.println(matchesPlayedPerYear);

    }
}
