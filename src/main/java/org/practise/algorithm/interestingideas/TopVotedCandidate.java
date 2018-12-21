package org.practise.algorithm.interestingideas;

import java.util.*;

/**
 * In an election, the i-th vote was cast for persons[i] at time times[i].
 *
 * Now, we would like to implement the following query function: TopVotedCandidate.q(int t) will return the number of the person
 * that was leading the election at time t.
 *
 * Votes cast at time t will count towards our query.  In the case of a tie, the most recent vote (among tied candidates) wins.
 *
 *
 *
 * Example 1:
 *
 * Input: ["TopVotedCandidate","q","q","q","q","q","q"], [[[0,1,1,0,0,1,0],[0,5,10,15,20,25,30]],[3],[12],[25],[15],[24],[8]]
 * Output: [null,0,1,1,0,0,1]
 * Explanation:
 * At time 3, the votes are [0], and 0 is leading.
 * At time 12, the votes are [0,1,1], and 1 is leading.
 * At time 25, the votes are [0,1,1,0,0,1], and 1 is leading (as ties go to the most recent vote.)
 * This continues for 3 more queries at time 15, 24, and 8.
 *
 *
 * Note:
 *
 * 1 <= persons.length = times.length <= 5000
 * 0 <= persons[i] <= persons.length
 * times is a strictly increasing array with all elements in [0, 10^9].
 * TopVotedCandidate.q is called at most 10000 times per test case.
 * TopVotedCandidate.q(int t) is always called with t >= times[0]
 */
public class TopVotedCandidate {

    // map candidate to vote count
    Map<Integer, Integer> mapCandidateVoteCount;

    // store leading candidate over time
    List<Vote> leadingCandidate;

    public TopVotedCandidate(int[] persons, int[] times) {
        mapCandidateVoteCount = new HashMap<>();
        leadingCandidate = new ArrayList<>();

        int maxIndividualVotes = 0, maxVotedCandidate = -1;

        for (int i = 0; i < persons.length; i++) {
            int candidate = persons[i];
            int candidateVotes = mapCandidateVoteCount.getOrDefault(candidate, 0) + 1;
            mapCandidateVoteCount.put(candidate, candidateVotes);

            if (candidateVotes >= maxIndividualVotes) {
                if (maxVotedCandidate != candidate) {
                    leadingCandidate.add(new Vote(candidate, times[i]));
                    maxVotedCandidate = candidate;
                }
                maxIndividualVotes = candidateVotes;
            }
        }
    }

    class Vote {
        int person;
        int time;
        public Vote(int person, int time) {
            this.person = person;
            this.time = time;
        }
    }

    public int q(int t) {
        int low = 0, high = leadingCandidate.size();
        while (low < high) {
            int mid = (low + high) / 2;
            if (leadingCandidate.get(mid).time <= t) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return leadingCandidate.get(low - 1).person;
    }

//    ALTERNATE APPROACH
//    TreeMap<Integer, Integer> tm;
//    public TopVotedCandidate(int[] persons, int[] times) {
//        int personCounts[] = new int[persons.length];
//        int leading = -1;
//        tm = new TreeMap<Integer, Integer>();
//        for(int i=0; i<times.length; i++) {
//            personCounts[persons[i]]++;
//            if(personCounts[persons[i]] >= leading) {
//                tm.put(times[i], persons[i]);
//                leading = personCounts[persons[i]];
//            }
//        }
//    }
//
//    public int q(int t) {
//        return tm.floorEntry(t).getValue();
//    }
}
