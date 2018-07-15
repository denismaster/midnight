package net.sf.l2j.gameserver.scripting.quests;

import java.util.HashMap;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;



public class Q999_TTutorial extends Quest
{
    public static class Talk
    {
        public int raceId;
        public String htmlfiles[];
        public int npcTyp;
        public int item;
        
        public Talk(int raceId, String htmlfiles[], int npcTyp, int item)
        {
            this.raceId = raceId;
            this.htmlfiles = htmlfiles;
            this.npcTyp = npcTyp;
            this.item = item;
        }
    }

    public static class Event
    {

        public String htm;
        public int radarX;
        public int radarY;
        public int radarZ;
        public int item;
        public int classId1;
        public int gift1;
        public int count1;
        public int classId2;
        public int gift2;
        public int count2;

        public Event(String htm, int radarX, int radarY, int radarZ, int item, int classId1, int gift1, 
                int count1, int classId2, int gift2, int count2)
        {
            this.htm = htm;
            this.radarX = radarX;
            this.radarY = radarY;
            this.radarZ = radarZ;
            this.item = item;
            this.classId1 = classId1;
            this.gift1 = gift1;
            this.count1 = count1;
            this.classId2 = classId2;
            this.gift2 = gift2;
            this.count2 = count2;
        }
    }


    public Q999_TTutorial()
    {
        super(999, "Q999_TTutorial");
        addStartNpc(new int[] {
            30009, 30019, 30131, 30575, 30530, 30400, 30401, 30402, 30403, 30404, 
            32133, 32134
        });
        addTalkId(new int[] {
            30008, 30017, 30018, 30020, 30021,30530, 30011, 30012, 30056, 30129, 30370, 
            30528, 30573, 30574
        });
        addFirstTalkId(new int[] {
            30600, 30601, 30602, 30598, 30599
        });
        addKillId(new int[] {
            18342
        });
        addKillId(new int[] {
            20001
        });
    }

    @Override
	public String onAdvEvent(String event, Npc npc, Player player)
    {
	
        QuestState st = player.getQuestState("Q999_TTutorial");
        QuestState qs = st.getPlayer().getQuestState(qnTutorial);
	
        if(qs == null){
	
		return null;}
		boolean isMage = player.isMageClass();
	
        int Ex = qs.getInt("Ex");
        
	
        int classId = player.getClassId().getId();
		
        if(event.equalsIgnoreCase("TimerEx_NewbieHelper"))
        {
			 if(Ex == 0)
            {
				 if(isMage==true)
                    st.playTutorialVoice("tutorial_voice_009b");
                else
                    st.playTutorialVoice("tutorial_voice_009a");
                qs.set("Ex", "1");
				
            } else
            if(Ex == 3)
            {
				 st.playTutorialVoice("tutorial_voice_010a");
                qs.set("Ex", "4");
			}
            return null;
        }
		
        if(event.equalsIgnoreCase("TimerEx_GrandMaster"))
        {
			 if(Ex >= 4 && st.getInt("Map") == 0)
            {
				st.showQuestionMark(7);
				st.playTutorialVoice("tutorial_voice_025");
                st.set("Map", "1");
            }
            return null;
        }
		
        String htmltext;
		
            Event e = events.get(event);
            if(e.radarX != 0)
                st.addRadar(e.radarX, e.radarY, e.radarZ);
            htmltext = e.htm;
            if(st.getQuestItemsCount(e.item) > 0)
            {
                st.rewardExpAndSp(0L, 50);
                startQuestTimer("TimerEx_GrandMaster", 60000, null, st.getPlayer(), true);
                st.takeItems(e.item, -1);
                if(Ex <= 3)
                    qs.set("Ex", "4");
                if(classId == e.classId1)
                {
                    st.giveItems(e.gift1, e.count1);
                    if(e.gift1 == SPIRITSHOT_NOVICE)
                        st.playTutorialVoice("tutorial_voice_027");
                    else
                        st.playTutorialVoice("tutorial_voice_026");
                } else
                if(classId == e.classId2 && e.gift2 != 0)
                {
                    st.giveItems(e.gift2, e.count2);
                    st.playTutorialVoice("tutorial_voice_026");
                }
            }
        
        return htmltext;
    }

    @Override
	public String onFirstTalk(Npc npc, Player player)
    {
		boolean isMage = player.isMageClass();
        QuestState qs = player.getQuestState(qnTutorial);
        if(qs == null)
            npc.showChatWindow(player);
        QuestState st = player.getQuestState(getName());
        if(st == null)
            st = newQuestState(player);
        int npcId = npc.getNpcId();
        if(npcId == 30600 || npcId == 30601 || npcId == 30602 || npcId == 30598 || npcId == 30599)
        {
            int reward = qs.getInt("reward");
            if(reward == 0)
            {
                if(isMage==true)
                {
                    qs.playTutorialVoice("tutorial_voice_027");
                    qs.giveItems(SPIRITSHOT_NOVICE, 100);
                } else
                {
                    st.playTutorialVoice("tutorial_voice_026");
                    st.giveItems(SOULSHOT_NOVICE, 200);
                }
                qs.set("reward", "1");
                st.set("onlyone", "1");
                st.giveItems(SCROLL, 2);
                st.giveItems(TOKEN, 12);
                st.exitQuest(false);
            }
        }
        npc.showChatWindow(player);
        return null;
    }

    @Override
	public String onTalk(Npc npc, Player player)
    {
		
        QuestState qs = player.getQuestState(qnTutorial);
        QuestState st = player.getQuestState(getName());
		
        if(st == null)
            st = newQuestState(player);
        String htmltext = "noquest";
        int npcId = npc.getNpcId();
		int Ex = qs.getInt("Ex");
		int onlyone = st.getInt("onlyone");
		int step = st.getInt("step");
		int level = player.getLevel();
		
        boolean isMage = player.isMageClass();
        Talk t = talks.get(Integer.valueOf(npcId));
        if(t == null)
            return null;
		if((level >= 10 || onlyone != 0) && t.npcTyp == 1)
            htmltext = "30575-05.htm";
        else
        if(level < 10 && onlyone == 0)
        {
            if(player.getRace().ordinal() == t.raceId)
            {
				
                htmltext = t.htmlfiles[0];
                if(t.npcTyp == 1)
                {
                    if(step == 0 && Ex < 0)
                    {
                        qs.set("Ex", "0");
                        startQuestTimer("TimerEx_NewbieHelper", 30000, npc, st.getPlayer(), true);
                        if(isMage)
                        {
                            st.set("step", "1");
                            st.setState(STATE_STARTED);
                            st.playSound("ItemSound.quest_tutorial");
                        } else
                        {
                            htmltext = "30530-01.htm";
                            st.set("step", "1");
                            st.setState(STATE_STARTED);
                            st.playSound("ItemSound.quest_tutorial");
                        }
                    } else
                    if(step == 1 && st.getQuestItemsCount(t.item) == 0 && Ex <= 2)
                    {
                        if(st.getQuestItemsCount(BLUE_GEM) > 0)
                        {
                            st.takeItems(BLUE_GEM, st.getQuestItemsCount(BLUE_GEM));
                            st.giveItems(t.item, 1);
                            st.set("step", "2");
                            qs.set("Ex", "3");
                            st.playSound("ItemSound.quest_middle");
                            startQuestTimer("TimerEx_NewbieHelper", 30000L, npc, st.getPlayer(), true);
                            qs.set("ucMemo", "3");
                            if(isMage==true)
                            {
                                st.playTutorialVoice("tutorial_voice_027");
                                st.giveItems(SPIRITSHOT_NOVICE, 100);
                                htmltext = t.htmlfiles[2];
                                if(htmltext.isEmpty())
                                    htmltext = "<html><body>I am sorry. I only help warriors. Please go to another Newbie Helper who may assist you.</body></html>";
                            } else
                            {
                                st.playTutorialVoice("tutorial_voice_026");
                                st.giveItems(SOULSHOT_NOVICE, 200);
                                htmltext = t.htmlfiles[1];
                                if(htmltext.isEmpty())
                                    htmltext = "<html><body>I am sorry. I only help mystics. Please go to another Newbie Helper who may assist you.</body></html>";
                            }
                        } else
                        if(isMage==true)
                        {
                            htmltext = "30131-02.htm";
                            if(player.getRace().ordinal() == 3)
                                htmltext = "30575-02.htm";
                        } else
                        {
                            htmltext = "30530-02.htm";
                        }
                    } else
                    if(step == 2)
                        htmltext = t.htmlfiles[3];
                } else
                if(t.npcTyp == 0)
                    if(step == 1)
                        htmltext = t.htmlfiles[0];
                    else
                    if(step == 2)
                        htmltext = t.htmlfiles[1];
                    else
                    if(step == 3)
                        htmltext = t.htmlfiles[2];
            }
        } else
        {
            htmltext = "<html><body>You are too experienced now.</body></html>";
        }
        return htmltext;
    }

    @Override
	public String onKill(Npc npc, Player player, boolean isPet)
    {
		QuestState st = player.getQuestState(getName());
        if(st == null)
            return super.onKill(npc, player, isPet);
        QuestState qs = st.getPlayer().getQuestState(qnTutorial);
        if(qs == null)
            return null;
        int Ex = qs.getInt("Ex");
		if(Ex <= 1)
        {
            st.playTutorialVoice("tutorial_voice_011");
            st.showQuestionMark(3);
            qs.set("Ex", "2");
		}
        if(st.getInt("step") == 1 && Ex <= 2 && st.getQuestItemsCount(BLUE_GEM) < 1)
        {
        	 if (Rnd.get(100) < 25)
             {
        		 ((Monster) npc).dropItem(player, new IntIntHolder(BLUE_GEM, 1));
            st.playSound("ItemSound.quest_finish");
             }
        }
        return null;
    }

    public static void onLoad()
    {
        new Q999_TTutorial();
    }

    private static String qnTutorial = "Tutorial";
    private static int RECOMMENDATION_01;
    private static int RECOMMENDATION_02;
    private static int LEAF_OF_MOTHERTREE;
    private static int BLOOD_OF_JUNDIN;
    private static int LICENSE_OF_MINER;
    private static int VOUCHER_OF_FLAME;
    private static int SOULSHOT_NOVICE;
    private static int SPIRITSHOT_NOVICE;
    private static int BLUE_GEM = 6353;
    private static int TOKEN = 8542;
    private static int SCROLL = 8594;
    private static int DIPLOMA;
    private static HashMap<String, Event> events;
    private static HashMap<Integer, Talk> talks;

    static 
    {
        RECOMMENDATION_01 = 1067;
        RECOMMENDATION_02 = 1068;
        LEAF_OF_MOTHERTREE = 1069;
        BLOOD_OF_JUNDIN = 1070;
        LICENSE_OF_MINER = 1498;
        VOUCHER_OF_FLAME = 1496;
        SOULSHOT_NOVICE = 5789;
        SPIRITSHOT_NOVICE = 5790;
        DIPLOMA = 9881;
        events = new HashMap<String, Event>();
        events.put("32133_02", new Event("32133-03.htm", 0xfffe2c74, 44504, 380, DIPLOMA, 123, SOULSHOT_NOVICE, 200, 124, SOULSHOT_NOVICE, 200));
        events.put("30008_02", new Event("30008-03.htm", 0, 0, 0, RECOMMENDATION_01, 0, SOULSHOT_NOVICE, 200, 0, 0, 0));
        events.put("30008_04", new Event("30008-04.htm", 0xfffeb7a6, 0x3b627, -3730, 0, 0, 0, 0, 0, 0, 0));
        events.put("30017_02", new Event("30017-03.htm", 0, 0, 0, RECOMMENDATION_02, 10, SPIRITSHOT_NOVICE, 100, 0, 0, 0));
        events.put("30017_04", new Event("30017-04.htm", 0xfffeb7a6, 0x3b627, -3730, 0, 10, 0, 0, 0, 0, 0));
        events.put("30370_02", new Event("30370-03.htm", 0, 0, 0, LEAF_OF_MOTHERTREE, 25, SPIRITSHOT_NOVICE, 100, 18, SOULSHOT_NOVICE, 200));
        events.put("30370_04", new Event("30370-04.htm", 45491, 48359, -3086, 0, 25, 0, 0, 18, 0, 0));
        events.put("30129_02", new Event("30129-03.htm", 0, 0, 0, BLOOD_OF_JUNDIN, 38, SPIRITSHOT_NOVICE, 100, 31, SOULSHOT_NOVICE, 200));
        events.put("30129_04", new Event("30129-04.htm", 12116, 16666, -4610, 0, 38, 0, 0, 31, 0, 0));
        events.put("30528_02", new Event("30528-03.htm", 0, 0, 0, LICENSE_OF_MINER, 53, SOULSHOT_NOVICE, 200, 0, 0, 0));
        events.put("30528_04", new Event("30528-04.htm", 0x1c3ba, 0xfffd4882, -941, 0, 53, 0, 0, 0, 0, 0));
        events.put("30573_02", new Event("30573-03.htm", 0, 0, 0, VOUCHER_OF_FLAME, 49, SPIRITSHOT_NOVICE, 100, 44, SOULSHOT_NOVICE, 200));
        events.put("30573_04", new Event("30573-04.htm", -45067, 0xfffe4473, -235, 0, 49, 0, 0, 44, 0, 0));
		
        talks = new HashMap<Integer, Talk>();
        talks.put(Integer.valueOf(30017), new Talk(0, new String[] {
            "30017-01.htm", "30017-02.htm", "30017-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30008), new Talk(0, new String[] {
            "30008-01.htm", "30008-02.htm", "30008-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30370), new Talk(1, new String[] {
            "30370-01.htm", "30370-02.htm", "30370-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30129), new Talk(2, new String[] {
            "30129-01.htm", "30129-02.htm", "30129-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30573), new Talk(3, new String[] {
            "30573-01.htm", "30573-02.htm", "30573-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30528), new Talk(4, new String[] {
            "30528-01.htm", "30528-02.htm", "30528-04.htm"
        }, 0, 0));
        talks.put(Integer.valueOf(30018), new Talk(0, new String[] {
            "30131-01.htm", "", "30019-03a.htm", "30019-04.htm"
        }, 1, RECOMMENDATION_02));
        talks.put(Integer.valueOf(30019), new Talk(0, new String[] {
            "30131-01.htm", "", "30019-03a.htm", "30019-04.htm"
        }, 1, RECOMMENDATION_02));
        talks.put(Integer.valueOf(30020), new Talk(0, new String[] {
            "30131-01.htm", "", "30019-03a.htm", "30019-04.htm"
        }, 1, RECOMMENDATION_02));
        talks.put(Integer.valueOf(30021), new Talk(0, new String[] {
            "30131-01.htm", "", "30019-03a.htm", "30019-04.htm"
        }, 1, RECOMMENDATION_02));
        talks.put(Integer.valueOf(30009), new Talk(0, new String[] {
            "30530-01.htm", "30009-03.htm", "", "30009-04.htm"
        }, 1, RECOMMENDATION_01));
        talks.put(Integer.valueOf(30011), new Talk(0, new String[] {
            "30530-01.htm", "30009-03.htm", "", "30009-04.htm"
        }, 1, RECOMMENDATION_01));
        talks.put(Integer.valueOf(30012), new Talk(0, new String[] {
            "30530-01.htm", "30009-03.htm", "", "30009-04.htm"
        }, 1, RECOMMENDATION_01));
        talks.put(Integer.valueOf(30056), new Talk(0, new String[] {
            "30530-01.htm", "30009-03.htm", "", "30009-04.htm"
        }, 1, RECOMMENDATION_01));
        talks.put(Integer.valueOf(30400), new Talk(1, new String[] {
            "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm"
        }, 1, LEAF_OF_MOTHERTREE));
        talks.put(Integer.valueOf(30401), new Talk(1, new String[] {
            "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm"
        }, 1, LEAF_OF_MOTHERTREE));
        talks.put(Integer.valueOf(30402), new Talk(1, new String[] {
            "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm"
        }, 1, LEAF_OF_MOTHERTREE));
        talks.put(Integer.valueOf(30403), new Talk(1, new String[] {
            "30131-01.htm", "30400-03.htm", "30400-03a.htm", "30400-04.htm"
        }, 1, LEAF_OF_MOTHERTREE));
        talks.put(Integer.valueOf(30131), new Talk(2, new String[] {
            "30131-01.htm", "30131-03.htm", "30131-03a.htm", "30131-04.htm"
        }, 1, BLOOD_OF_JUNDIN));
        talks.put(Integer.valueOf(30404), new Talk(2, new String[] {
            "30131-01.htm", "30131-03.htm", "30131-03a.htm", "30131-04.htm"
        }, 1, BLOOD_OF_JUNDIN));
        talks.put(Integer.valueOf(30574), new Talk(3, new String[] {
            "30575-01.htm", "30575-03.htm", "30575-03a.htm", "30575-04.htm"
        }, 1, VOUCHER_OF_FLAME));
        talks.put(Integer.valueOf(30575), new Talk(3, new String[] {
            "30575-01.htm", "30575-03.htm", "30575-03a.htm", "30575-04.htm"
        }, 1, VOUCHER_OF_FLAME));
        talks.put(Integer.valueOf(30530), new Talk(4, new String[] {
            "30530-01.htm", "30530-03.htm", "", "30530-04.htm"
        }, 1, LICENSE_OF_MINER));
        talks.put(Integer.valueOf(30598), new Talk(0, null, 0, 0));
        talks.put(Integer.valueOf(30599), new Talk(0, null, 0, 0));
        talks.put(Integer.valueOf(30600), new Talk(0, null, 0, 0));
        talks.put(Integer.valueOf(30601), new Talk(0, null, 0, 0));
        talks.put(Integer.valueOf(30602), new Talk(0, null, 0, 0));
    }
}
