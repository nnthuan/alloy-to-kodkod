open util/ordering [Time] as T

sig Time {}

sig Game 
			{ 
 					player: one Farmer , 
 					scene: set Scene 
 			}


sig Scene 
			{
					name: one String,
					flowerlist: some Flower,
					toolist: some Tool 		
			}


sig Farmer
			{
					resource: Int one -> one Time,

			}

sig Flower
			{					
					 
					score:  Int one -> one Time,
					bonus: one Int
			} 

sig Rose extends Flower
			{
					type: one Int
									
			}

/*			{
					all t:Time , score = 0 || state = HarvestedState || state = GrowingState && this.score.t >= 3
			}
*/

sig Tool
			{
					score: Int one  -> one Time,
				
			}

sig Hoe extends Tool
		{
				type: one String
		}

sig Sprinkler extends Tool
		{
				capacity: one Int
		}

// define rule
fact HarvestingRose  
		{
				all t: Time, g: Game,  r: Rose | g.player.resource.t = g.player.resource.t + r.score.t  iff r.score.t = 3
		}

pred init[t: Time]
{
		Game.player.resource.t = Tool.score.t &&  Flower.score.t=0
}

pred checkWin[f: set Farmer,  t, t': Time]
	{
		all fm: Farmer - f | 
		fm.resource.t' = fm.resource.t + 3		 
	 }

pred maingame[ t0, t1, t2, t3, t4: Time,  r1: set Rose, h1: set Hoe, f: set Farmer ]
		{
			// 
		
					/*		t0 != t1  && t0 != t2 && t0 != t3 && t0 != t4
										  && t1 != t2 && t1 != t3 && t1 != t4
															 && t2 != t3 && t2 != t4
																				&& t3 != t4 		
					*/
						let t1=t0.next, t2=t1.next, t3=t2.next, t4=t3.next	

							-- precondition
							{
							//init[t0]
							f.resource.t0 = 3 
							r1.score.t0 = 1 
							h1.score.t0=3 
								
						--process
						some f2: Farmer - f | 
							f2.resource.t1 = f.resource.t0-1    //  sowing
							r1.score.t1 = r1.score.t0+1 
							h1.score.t1 = h1.score.t0 
						some f2: Farmer - f |	
							f2.resource.t2 = 2 // growing
							r1.score.t2 = 3
							h1.score.t2 = 2 
						some f2: Farmer - f |
							f2.resource.t3 = 5 //harvesting
							r1.score.t3 = 0 
							h1.score.t3 = 2


							f.resource.t4 = 4 // sowing
							r1.score.t4 = 1 
							h1.score.t4 = 2 
							}
							--post-condition
							//checkWin[f, t0, t1]
					
							--- condition frame
		}


run maingame for 6 but 5 Time, 1 Rose, 1 Hoe, 1 Farmer, 6 Int 
//check maingame{}
// run checkWin for 3 but 2 Time, 1 Farmer

//run checkWin
