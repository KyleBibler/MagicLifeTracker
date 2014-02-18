package com.example.magiclifetracker;

/*
 * Kyle Bibler
 * MagicLifeTracker
 * -------------------------------------------------------------------
 * Magic the Gathering Life Tracking application
 * Can add variable numbers of players and delete them
 * Tracks life totals where you can add and subtract manually
 * Also tracks poison counters
 * Does not have auto loss detection because there are so many exceptions
 * 
 * Because I need this now, using the standard android art assets. Blah 
 * --------------------------------------------------------------------
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private LinearLayout screen;
	private boolean defaultsInitialized;	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		screen = (LinearLayout) findViewById(R.id.screenLayout);
		screen.setOrientation(LinearLayout.VERTICAL);

		TextView label = new TextView(this);
		label.setText("Players: ");
		label.setTextSize(28);
		screen.addView(label);
		
		defaultsInitialized = false;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case R.id.addPlayer:
			addPlayerMenuItem();
			break;	
		case R.id.delete:
			deleteMenuItem();
			break;	
		case R.id.defaultPlayers:
			defaultPlayersMenuItem();
			break;		
		case R.id.action_settings:
			settingsMenuItem();
			break;
		case R.id.quit:
			quitMenuItem();
			break;
		case R.id.remove_all:
			removeAllMenuItem();
			break;
		}

		return true;
	}
	
	/*
	 * Quits the activity, 
	 * same thing can be accomplished by just pressing the back button.
	 */
	private void quitMenuItem() {
		
		final LinearLayout layout = new LinearLayout(this);
		
		final TextView text = new TextView(this);
		text.setText("Are you Sure?");
		text.setTextSize(30);
		text.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(text);
		layout.setGravity(Gravity.CENTER);
		
		new AlertDialog.Builder(this)
		.setTitle("Quit")
		.setView(layout)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				finish();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//close
			}
		}).show();		
	}


	/*
	 * Removes all players in the view.
	 */
	private void removeAllMenuItem() {
		screen.removeAllViews();
		TextView label = new TextView(this);
		label.setText("Players: ");
		label.setTextSize(28);
		screen.addView(label);
		defaultsInitialized = false;
	}



	/*
	 * Creates a settings menu, currently it only has one setting
	 * that is editable, can add more later (if there are any more?)	 * 
	 */
	private void settingsMenuItem() {
		
		final LinearLayout layout = new LinearLayout(this);
		
		//creates a button for changing the only setting
		//which is whether defaults can be enabled more than once.
		final Button defaults = new Button(this);
		final TextView allow = new TextView(this);
		allow.setText("Allow Defaults: ");
		allow.setTextSize(24);
		layout.addView(allow);
		defaults.setText("" + !defaultsInitialized);
		defaults.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultsInitialized = !defaultsInitialized;
				defaults.setText("" + !defaultsInitialized);				
			}			
		});		
		
		layout.addView(defaults);
		
		//pops up a little setting menu
		new AlertDialog.Builder(this)
		.setTitle("Settings")
		.setView(layout)
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//closes Alert dialog
			}
		}).show();
		
	}



	/*
	* Adds a new layout to the main layout 
	* Adds a new PlayerLayout object with name from the input
	* Usually because not too many players are added (15+ player games don't happen)
	* Did not use a scrollview for main layout
	*/
	private void addPlayerMenuItem() {
		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText nameEdit = new EditText(this);
		//final TextView enterName = new TextView(this);	

		//Set input style to capitalize individual words (how names are usually written)
		nameEdit.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		layout.addView(nameEdit);
		
		//Pops up a place to input player names, then creates and adds PlayerLayout
		new AlertDialog.Builder(this)
		.setTitle("Enter Player Name")
		.setView(layout)
		.setNeutralButton("Set", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText player = (EditText) layout.getChildAt(0);
				screen.addView(new PlayerLayout(getApplicationContext(), player.getText().toString()));
			}
		}).show();
	}

	/*
	* Deletes a PlayerLayout object from the main layout
	* To be implemented later...NOW IT IS IMPLEMENTED HUZZAH
	*/
	private void deleteMenuItem() {		
		final LinearLayout layout = new LinearLayout(this);	
		layout.setOrientation(LinearLayout.VERTICAL);
		
		int numChildren = screen.getChildCount() - 1;  //Subtract one to account for Players Label
		final PlayerLayout[] children = new PlayerLayout[numChildren];
		final int[] childIds = new int[numChildren];
		
		for (int i = 0; i < numChildren; i++) {			
			children[i] = (PlayerLayout) screen.getChildAt(i+1); 
		}
		
		//Adds delete buttons for every PlayerLayout in main view
		for(int i = 0; i < numChildren; i++) { 
			final PlayerLayout child = children[i];
			childIds[i] = child.getId();
			final Button deleteButton = new Button(this);
			deleteButton.setText(child.getName());
			deleteButton.setTextSize(24);
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
						screen.removeView(child);
						layout.removeView(deleteButton);
				}				
			});
			layout.addView(deleteButton);
		}
		
		//Pops up a dialog where you can delete any player currently in the view
		new AlertDialog.Builder(this)
		.setTitle("Delete Player")
		.setView(layout)
		.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//closes alert dialogue
			}
		}).show();
	}

	/*
	* Because I don't want to type out all of our names every time
	* and that I play with the same people every time
	* This creates 5 PlayerLayouts automatically if they haven't already been initialized
	* Could add functionality to change default players
	*/
	private void defaultPlayersMenuItem() {
		if (!defaultsInitialized) {
			String[] names = {"Kyle", "Will", "John", "Mike", "Paul"};
			for (int i = 0; i < names.length; i++) {
				screen.addView(new PlayerLayout(getApplicationContext(), names[i]));
			}
			defaultsInitialized = true;
		}
	}


	/*
	* Sets up a new layout class, PlayerLayout
	* Each PlayerLayout has Name: (+) Life: # (-)  PoisonCounters: #
	* There are too many exceptions in game so didn't add any lose functionality
	* If I get to it, make the Layouts look the same in all cases.
	*/
	private class PlayerLayout extends LinearLayout {

		private int lifeTotal;
		private int poison;
		private String name;

		public PlayerLayout(Context context, String name) {
			super(context);
			this.name = name;
			lifeTotal = 20;
			poison = 0;
			this.initialize(context);			
		}

		public void initialize(Context context) {
			this.setOrientation(LinearLayout.HORIZONTAL);

			final TextView lifeView = new TextView(context);
			lifeView.setText("Life: " + lifeTotal);
			lifeView.setTextSize(20);
			lifeView.setTextColor(Color.BLACK);

			final TextView nameView = new TextView(context);
			nameView.setText(name + "\t");
			nameView.setTextSize(20);
			nameView.setTextColor(Color.BLACK);
			this.addView(nameView);
		
			//Button to add life
			Button addButton = new Button(context);
			addButton.setText("+");
			addButton.setTypeface(Typeface.DEFAULT_BOLD);
			addButton.setTextSize(24);
			addButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					lifeTotal++;
					lifeView.setText("Life: " + lifeTotal);					
				}				
			});

			//Button to subtract life
			final Button subButton = new Button(context);
			subButton.setText("-");
			subButton.setTextSize(24);
			subButton.setTypeface(Typeface.DEFAULT_BOLD);
			subButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					lifeTotal = lifeTotal - 1;
					if (lifeTotal <= 0) {
						lifeTotal = 0;
					}
					lifeView.setText("Life: " + lifeTotal);					
				}				
			});

			//Button to add poison counters (10 poison counters and you lose...usually)
			final Button poisonBut = new Button (context);
			poisonBut.setText("P: " + poison);
			poisonBut.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(poison < 10) {
						poison++;
						poisonBut.setText("P: " + poison);	
					}				
				}				
			});
			poisonBut.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(poison > 0) {
						poison--;
						poisonBut.setText("P: " + poison);	
					}
					return true;
				}			
			});

			this.addView(addButton);
			this.addView(lifeView);
			this.addView(subButton);	
			this.addView(poisonBut);
		}
		
		public String getName() {
			return name;
		}

	}

}
