package dev.menace.module.modules.misc;

import java.awt.Color;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.notif.Notification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;

public class StaffDetector extends Module {

	String[] staff = {"vFahad_", "iDhoom", "Jinaaan", "_JustMix", "Eissaa", "1Mhmmd",
			"mohmad_q8", "1Brhom", "AssassinTime", "Aliiyah", "PerfectRod_", "Ahmmd",
			"xImTaiG_", "xIBerryPlayz", "comsterr", "1Sweet", "Ev2n", "1M7mdz", 
			"1F5aMH___3oo", "iMehdi_", "xMz7", "EyesO_Diamond", "1Daykel", "Aboz3bl",
			"qB6o6", "Luvaa", "Boviix", "xImMuntadher_", "ZANAD", "ixBander", "WalriderTime",
			"Saajed", "Thenvra", "CutieRana", "Bo6lalll", "DestroyerOnyc_", "MK_F16",
			"1HeyImHasson_", "zayedk", "rcski", "iiRaivy", "leeleeeleeeleee", "baderr", "M7mmd",
			"M4rwaan", "lovelywords", "Creegam", "Bastic", "qMabel", "iLuvSG_", "1Narwhql",
			"TheDaddyJames", "iikimo", "iS3od_", "1M7mmD", "Yaazzeed", "Sadlly", "MindOfNasser",
			"ebararh", "F2rris", "1LaB", "Aymann_", "Maarcii", "1Ahmvd", "Flineer", "Refolt", 
			"Y2men", "Dizibre", "wishingdeath", "RADVN", "KaaReeeM", "MightyM7MD", "ritclaw", 
			"EVanDoskI", "_N3", "Zqvies", "1Pepe_", "8mhh", "Mdqz", "1RealFadi", "Driction", 
			"GlND", "xDupzHell", "0hPqnos", "Wesccar", "Requieem", "Fta7", "DeeRx", "Y_04", 
			"1Tz3bo", "xL2d", "7re2a_YT", "Tibbz_BGamer", "PT7", "1Mshari", "ImXann", 
			"phxnomenal", "7MZH", "arbawii", "1_ST", "SalemBayern_", "iSolom", "qPito",
			"JustDrink_", "xIMonster_Rj", "Tibbz_BGamer_", "Tostiebramkaas", "beyondviolets",
			"S3rvox", "mzh", "DangPavel", "lacvna", "Banderr", "Oxenaa", "0Da3s", "iRxv", "Jrx7",
			"ilybb0", "ilybb0", "inVertice", "w7r", "STEEEEEVEEEE", "lt1x", "itzZa1D", "1Kw3zfTea_",
			"KingHOYT", "vdhvm", "TheDrag_Xx", "Mhmovd", "_Vxpe", "_SpecialSA_", "Ittekimasu", "vxom",
			"iAhmedGG", "1M0ha", "InjjectoR", "1Omxr", "Punshmit", "AFG_progamer92",
			"_R3", "yQuack", "Ba1z", "lwra", "xiDayzer", "1DeVilz", "Dqrkfall", "BlackOurs",
			"iA11", "1Loga_", "Futurezii", "DaBabyFan", "1KhaleeD", "1LoST_", "A5oShnBaT",
			"AbuA7md506", "0Aix", "Blood_Artz", "ToFy_", "Ily_W6n", "IxDjole", "DestroyerTrUnKs",
			"SpecialAdel_", "yff3", "1L7NN", "1A7mad1", "qlxc", "wzii", "1RE3", "DarkA5_", "Raceth", 
			"FexoraNEP", "0h_Roby", "obaida123445", "1Kweng", "_b_i", "OldAlone", "vinnythebot", "1_aq", 
			"Mjdra_call_ME", "d5qq", "Mark_Gamer_YT_", "SamoXS", "yosife_7Y", "s2lm", "AfootDiamond117", 
			"mokgii", "lareey", "tverdy", "BaSiL_123", "G3rryx", "Its_HighNoon", "Haifa_magic", "MaybeHeDoes",
			"IxKimo", "real__happy", "Cryslinq", "wl3d", "AsgardOfEddard", "IR3DX", "Mythiques", "Mondoros", 
			"bota_69", "1Levaai", "manuelmaster", "1Sinqx", "xLePerfect", "ogm", "_Ottawa", "_0bX", "90fa", 
			"Bo3aShor", "SpecialAdam_", "BinDontCare", "i_Ym5", "Dedz1k", "Lemfs", "ttkshr_", "9we", "Kuhimitsu", 
			"uh8e", "Sp0tzy_", "Aboal3z14", "_xayu_", "0DrRep", "H2ris", "abd0_369", "Just7MO", "Lunching", "zCroDanger",
			"vBursT_", "HM___", "Tabby_Bhau", "Tetdeus", "rqnkk", "Neeres", "_z2_", "RealWayne", "Jarxay", "AwKTaM", "IDoubIe", 
			"Du7ym", "INFAMOUSEEE", "Alaam_FG", "xDiaa_levo", "vM6r", "redcriper", "Draggn_", "3rodi", "sh5boo6", "kostasidk", 
			"TheOnlyM7MAD", "iiEsaTKing_", "AhmedPROGG", "_iSkyla", "1Derex"};

	public StaffDetector() {
		super("StaffDetector", 0, Category.MISC);
	}

	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S19PacketEntityHeadLook) {
			S19PacketEntityHeadLook packet = (S19PacketEntityHeadLook) event.getPacket();
			Entity entity = packet.getEntity(MC.theWorld);
			for (String s : staff) {
				if (entity.getName().equalsIgnoreCase(s)) {
					Menace.instance.notificationManager.addNotification(new Notification(entity.getName() + "detected for being a staff!", Color.RED, 300L));
					MC.thePlayer.sendChatMessage("/leave");
				}
			}
		}
	}
}
