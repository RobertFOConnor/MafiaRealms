package com.yellowbyte.giovannifallout.appwarp;

import org.json.JSONObject;


public class WarpSender {

	public static final int ENDED_TURN = 0;
	public static final int SAC_RESOURCE = 1;
	public static final int SAC_CARDS = 2;
	public static final int SUMMONED_UNIT = 3;
	public static final int PLAYED_ACTION = 4;
	public static final int PLAYED_EVENT = 5;
	public static final int SURRENDER = 6;
	public static final int MOVED_UNIT = 7;
	
	public void sendTurn() {
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", ENDED_TURN);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: End Turn!");

		} catch (Exception e) {
			
		}
	}
	
	public void sendResourceSacrifice() {
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", SAC_RESOURCE);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Sacrificed Resource!");

		} catch (Exception e) {
			
		}
	}
	
	public void sendCardSacrifice() {
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", SAC_CARDS);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Sacrificed Card!");

		} catch (Exception e) {
			
		}
	}
	
	
	public void sendSummon(int row, int col, int cardID) {
		
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", SUMMONED_UNIT);
			
			data.put("row", row);
			data.put("col", col);
			data.put("cardID", cardID);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Summon!");

		} catch (Exception e) {
			
		}
	}
	
	public void sendMove(int oldRow, int oldCol, int row, int col, int cardID) {
		
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", MOVED_UNIT);
			data.put("oldRow", oldRow);
			data.put("oldCol", oldCol);
			data.put("row", row);
			data.put("col", col);
			data.put("cardID", cardID);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Placement!");

		} catch (Exception e) {
			
		}
	}
	
	public void sendApplyCard(int row, int col, int cardID, boolean myGrid) {
		
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", PLAYED_ACTION);
			
			data.put("row", row);
			data.put("col", col);
			data.put("cardID", cardID);
			data.put("myGrid", myGrid);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Apply Card!");

		} catch (Exception e) {
			
		}
	}
	
	public void sendApplyEventCard(int cardID) {
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", PLAYED_EVENT);
			
			data.put("cardID", cardID);

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Event Card!");

		} catch (Exception e) {
			
		}
	}
	
	
	public void sendSurrender() {
		
		try {
			JSONObject data = new JSONObject();
			data.put("updateType", SURRENDER);
			

			WarpController.getInstance().sendGameUpdate(data.toString());
			System.out.println("Sender: Sent Surrender!");

		} catch (Exception e) {
			
		}
	}
}
