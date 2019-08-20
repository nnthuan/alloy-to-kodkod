module ModilDSL 

open util/ordering [Time] as T

sig Time
		{
		}

sig Game 
			{ 
 					player: one Farmer , 
 					scene: set Scene ,
					state: set StateGame one -> one Time
 			}

sig StateGame
			{
							values: Int one ->one Time
			}

one sig StateWin, StateFail, StateNormal extends StateGame{}

sig Scene 
			{
					name: one String,
					flowerlist: some Flower,
					toolist: some Tool 		
			}

one sig ScoreWin, ScoreFail, ScoreNormal
		{
				values: one Int
		}

sig Farmer
			{
					resource: Int one -> one Time,
					
			}

sig FlowerState {}

one sig  InitState, SowedState, GrowingState, HarvestedState extends FlowerState {}

sig Flower
			{					
					 
					score:  Int one -> one Time,
					bonus: one Int,
					maturity: one Int,
			} 

sig Rose extends Flower
			{
					type: one Int
									
			}
/*
			{
					 (state = InitState && score = 0) iff (state = HarvestedState || state = GrowingState && maturity >= 3)
			}
*/
sig Tool
			{
					score: Int one  ->one Time,
					icon: one String,
					enforced: some Flower,
					apply: some Rule
			}

sig Hoe extends Tool
		{
				type: one String
		}

sig Sprinkler extends Tool
		{
				capacity: one Int
		}

sig Rule
		{ 
				cost: one Int,
				scope: one Int,
				content: one String
					
		}

sig Rulegrowing extends Rule
		{
				
		}

sig Rulesowing extends Rule
		{
				
		}

sig Ruleharvesting extends Rule
		{
				
		}


pred  init[t: Time]
	{
			no Farmer.resource.t
 			

	}

// FRAME CONDITIONS

pred noFlower[t, t': Time] 
	{	 
		 
	}

pred noTool[t, t': Time] 
	{	 
		 
	}
 
pred overScore[f: Farmer, t,t': Time] 
	{ 
			 
	} 
pred noResource[f: Farmer,  t,t': Time] 
	{ 
		
	} 

pred checkWin[f: set Farmer,  t,t': Time]
	{
		all fm: Farmer - f| 
		fm.resource.t' = fm.resource.t+3
		 
	}

pred checkFail[f: Farmer,  fl: Flower,  s:  ScoreFail,  t,t': Time]
	{
		
	}

pred checkNormal[f: Farmer,  fl: Flower,  s: ScoreNormal,  t,t': Time]
	{
			
		
	}

// Define Rule 1
fact InitToolHoe
		{
				all t: Time, h: Hoe |  h.type = "SHORT"  &&  h.score.t = 1 

		}

fact InitToolSprinkler
		{
				all t: Time, s: Sprinkler | s.capacity = 5 && s.score.t = 1 
		 }

fact Initplant 
		{
										
					all t: Time, f: Flower, g: Game | f.score.t= 1 iff g.player.resource.t >=  f.score.t		
		 }

fact Sowingplant 
		{
				all t: Time, f: Flower, g: Game | f.score.t = 2 iff (g.player.resource.t >= f.score.t && f.score.t = 1)						
		}

fact Growingplant 
		{
				all t: Time, f: Flower, g: Game | f.score.t = 3 && f.score.t = f.score.t + f.bonus iff (g.player.resource.t >= 1) 						
		}


fact Harvestingplant 
		{
				all t: Time, f: Flower, g: Game | g.player.resource.t = g.player.resource.t + f.score.t  iff  f.score.t = 4					
		}

// Define Rule 2
fact GameOver 
		{
				all t:Time, g: Game | g.state.t = StateFail iff g.player.resource.t <= 0
		}

fact GameWin 
		{
				all t: Time, g: Game | g.state.t = StateWin && gte[g.player.resource.t ,5]
		}

fact GameNormal 
		{
				all t: Time, g: Game | g.state.t = StateNormal && lt[g.player.resource.t ,5] && gt[g.player.resource.t,0]
		}

fact SowingRose  
		{
					all t:Time,  g: Game,  r: Rose, h: Hoe  | (g.player.resource.t = g.player.resource.t - 1  && r.score.t = 2 && h.score.t = h.score.t - 1)
																						iff r.score.t = 1 && h.score.t >= 1 
		}

fact GrowingRose 
		{
					all t:Time, g: Game,  r: Rose, s: Sprinkler | g.player.resource.t = g.player.resource.t - 1  && r.score.t = r.score.t + 2  
																			&& r.score.t = 2 && s.score.t = s.score.t - 1 
																			iff r.score.t = 2 || r.score.t = 2 && s.score.t >= 1
		}

fact HarvestingRose  
		{
					all t: Time, g: Game,  r: Rose | g.player.resource.t = g.player.resource.t + r.score.t  iff r.score.t = 3
		}


// DEFINE RULE 4


fact HarvestingRoseRule 
		{
						all t: Time, r:Rose | r.score.t = 3 iff r.score.t = 2 && r.score.t >= 4
		}

// DEFINE RULE 5

fact HarvestingRoseRule 
		{
							all r: Rose, t: Time | r.score.t = 1 && r.score.t = 0 iff r.score.t = 3 || r.score.t =2 
		}

//DEFINE RULE 6

pred testgame1(g: Game, f: Farmer)
		{
			//	f.score = 5 && g.player = f
		}


pred actiongame[ t, t': Time,   fl: Flower, h1: Tool, f: Farmer ]
		{
					f.resource.t' = f.resource.t - 1
					fl.score.t' = fl.score.t + 2
					h1.score.t' = h1.score.t-1
		}

//pred maingame[ t0, t1, t2, t3, t4: Time,   r1: Rose, h1: Tool, f: Farmer ]
//	{
/*
	some t: Time | 
	some m: Man | some w: Woman | 
	//let t' = T/next [t] | 

	let t' = t.next | 
	marriage [m, w, t, t']
*/

pred maingame
		{
					some t0: Time |
					some r1: Rose | some h1: Tool | one f: Farmer|
					let t1=t0.next |
					actiongame [t0, t1, r1, h1, f] 

					
	
/*						let t1=t0.next, t2=t1.next, t3=t2.next, t4=t3.next	
						{
					//t0 != t1  && t1 != t2 && t2 != t3 && t3 != t4

							-- precondition
							f.resource.t0 = 3 
							r1.score.t0 = 1
							h1.score.t0=3
						
						--process
							f.resource.t1 = 2   //  sowing
							r1.score.t1 = 2
							h1.score.t1 = 3
				
							f.resource.t2 = 2// growing
							r1.score.t2 = 3
							h1.score.t2 = 2

							f.resource.t3 = 5 //harvesting
							r1.score.t3 = 0
							h1.score.t3 = 2


							f.resource.t4 = 4
							r1.score.t4 = 1
							h1.score.t4 = 2
					}*/

  		}

//run maingame for 5 but 5 Time, 1 Rose, 1 Tool, 1 Farmer, 6 Int  
run maingame{} for 5 but 2 Time, 1 Rose, 1 Tool, 1 Farmer, 6 Int

/*assert Testgameagain 
{
			some t1: Time,  ro: Rose, ro': Rose - ro, 
					h: Hoe, h': Hoe - h,  r: Farmer, r': Farmer - r |
					let t2=t1.next, t3=t2.next, t4=t3.next, t5=t4.next 
					{
						actiongame [t1, t2, ro, h, r]
						actiongame [t2, t3, ro', h, r]
						actiongame [t3, t4, ro, h, r]
						actiongame [t4, t5, ro, h, r']
					}
	}

*/
//check Testgameagain 

//run maingame for 5 but 5 Time, 1 Rose, 1 Tool, 1 Farmer 
