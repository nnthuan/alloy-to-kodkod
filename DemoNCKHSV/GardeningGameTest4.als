 open util/ordering [Time] as TO

sig Time {}

sig GameState {}
	one sig WON, GAMEOVER, PLAYING extends GameState {}

sig GameEvent {}
one sig GROWING, SOWING, HARVESTING, SLEEPING extends GameEvent {}		
 
one sig Gardener {	
	resource: set (Int one -> Time),
	game_state: set (GameState one -> Time),
	event: set (GameEvent one -> Time)
 } {all t: Time | resource.t >= 0}

sig Flower {	
	score:  set (Int one ->  Time)
 } {all t: Time | score.t >= 0 && score.t <= 2}

sig Rose, Tulip, Sunflower extends Flower {	}

sig Tool {	
	cost: set (Int one ->  Time)	
 } {all t: Time | cost.t >= 0 && cost.t <= 3}

sig Hoe, Sprinkler, Scissors extends Tool {}

// define rule of the state of game  
fact gamestate{
//won 
all t: Time,  ga: Gardener | ga.resource.t >= 4 
 => ga.game_state.t = WON
// gameover
all t: Time, ga: Gardener | ga.resource.t <= 0 
 => ga.game_state.t = GAMEOVER
// gaming
all t: Time, ga: Gardener | ga.resource.t > 0 
 && ga.resource.t < 4 => ga.game_state.t = PLAYING}

fact sowing {
// correct gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, h: Hoe, ga: Gardener, to: Tool-h | t' = TO/next[t] && fl.score.t = 0 && h.cost.t > 0 && ga.event.t = SOWING implies 
		fl.score.t' = plus[int fl.score.t, 1] && h.cost.t' = minus[int h.cost.t, 1] && ga.resource.t' = plus[int ga.resource.t, 0] && 
		to.cost.t' = plus[int to.cost.t, 0]
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t = 0 && to.cost.t >= 0 && ga.event.t = GROWING implies 
		fl.score.t' = plus[int fl.score.t, 0] && to.cost.t' = plus[int to.cost.t, 0] && ga.resource.t' = plus[int ga.resource.t, 0]
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t = 0 && to.cost.t >=	 0 && ga.event.t = HARVESTING implies 
		fl.score.t' = 0 && to.cost.t' = minus[int to.cost.t, 0] && ga.resource.t' = plus[int ga.resource.t, 0]
}

fact growing {
// correct gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, sc: Sprinkler, ga: Gardener, to: Tool-sc | t' = TO/next[t] && fl.score.t > 0 && fl.score.t < 2 && sc.cost.t > 0 && ga.event.t = GROWING implies 
		fl.score.t' = plus[int fl.score.t, 1] && sc.cost.t' = minus[int sc.cost.t, 1] && ga.resource.t' = plus[int ga.resource.t, 0]
		&& to.cost.t' = plus[int to.cost.t, 0]
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t > 0 && fl.score.t < 2 && to.cost.t >= 0 && ga.event.t = SOWING implies 
		fl.score.t' = plus[int fl.score.t, 0] && to.cost.t' = minus[int to.cost.t, 0] && ga.resource.t' = plus[int ga.resource.t, 0]
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t > 0 && fl.score.t < 2 && to.cost.t >= 0 && ga.event.t = HARVESTING implies 
		fl.score.t' = plus[int fl.score.t, 0] && to.cost.t' = minus[int to.cost.t, 0] && ga.resource.t' = plus[int ga.resource.t, 0]}


fact harvesting {
// correct gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, sc: Scissors, ga: Gardener, to: Tool-sc | t' = TO/next[t] && fl.score.t >= 2 && sc.cost.t > 0 &&
		ga.event.t = HARVESTING implies 
		fl.score.t' = 0 && ga.resource.t' = plus[int ga.resource.t, int fl.score.t] 
		&& sc.cost.t' = minus[int sc.cost.t, 1] && to.cost.t' = plus[int to.cost.t, 0] 
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t >= 2 && to.cost.t >= 0 &&
		ga.event.t = SOWING implies 
		fl.score.t' = minus[int fl.score.t,0] && ga.resource.t' = plus[int ga.resource.t,0] && to.cost.t' = minus[int to.cost.t, 0]
// invalid gardening event
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t >= 2 && to.cost.t >= 0 &&
		ga.event.t = GROWING implies 
		fl.score.t' = minus[int fl.score.t, 0] && ga.resource.t' = plus[int ga.resource.t, 0] && to.cost.t' = minus[int to.cost.t, 0]}

// define rule when gardener is sleeping
fact sleeping {
// Sleeping when flower is harversting 
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && fl.score.t >= 2 && ga.event.t = SLEEPING implies 
		fl.score.t' = 0 && ga.resource.t' = plus[int ga.resource.t, 0] && to.cost.t' = plus[int to.cost.t, 0]
// Sleeping when not happen 
all t: Time - last, t': Time,  
	 	fl: Flower, to: Tool, ga: Gardener | t' = TO/next[t] && ga.event.t = SLEEPING implies 
		fl.score.t' = fl.score.t && to.cost.t' = to.cost.t && ga.resource.t = ga.resource.t'}


// define rule buy something
fact tooling {
//buy scissor
all t: Time - last, t': Time,  
	 	fl: Flower, sc: Scissors, to: Tool-sc, ga: Gardener | t' = TO/next[t] && fl.score.t >= 2 && ga.event.t = HARVESTING && sc.cost.t = 0 implies 
		fl.score.t' = fl.score.t && to.cost.t' = to.cost.t && ga.resource.t' = minus[ga.resource.t,2] && sc.cost.t' = plus[sc.cost.t,2]

//buy hoe
all t: Time - last, t': Time,  
	 	fl: Flower, h: Hoe, to: Tool-h, ga: Gardener | t' = TO/next[t] && fl.score.t = 0 && ga.event.t = SOWING && h.cost.t = 0 implies 
		fl.score.t' = fl.score.t && to.cost.t' = to.cost.t && ga.resource.t' = minus[ga.resource.t, 2] && h.cost.t' = plus[h.cost.t, 2]

//buy sprinkler
all t: Time - last, t': Time,  
	 	fl: Flower, sp: Sprinkler, to: Tool-sp, ga: Gardener | t' = TO/next[t] && fl.score.t < 2 &&fl.score.t > 0 && ga.event.t = GROWING && sp.cost.t = 0 implies 
		fl.score.t' = fl.score.t && to.cost.t' = to.cost.t && ga.resource.t' = minus[ga.resource.t,2] && sp.cost.t' = plus[sp.cost.t,2]
}

pred initGame[ ga: Gardener, rs: Rose, tl: Tulip, sun: Sunflower,  to: Hoe, sp: Sprinkler, Sc: Scissors, t0, t1, t2, t3, t4, t5, t6, t7: Time] {
	let t0 = first, t1 = t0.next, t2 = t1.next, t3 = t2.next,
			t4 = t3.next, t5 =t4.next, t6 = t5.next, t7 = last	
		{
	//init
	int to.cost.t0 = 1
	int sp.cost.t0 = 1
	int Sc.cost.t0 = 1
	int ga.resource.t0 = 3
	int tl.score.t0 = 0
	int rs.score.t0 = 0
	int sun.score.t0 = 0
	ga.event.t0 = SOWING
	
	ga.event.t1 = GROWING
	ga.event.t2 = HARVESTING
	ga.event.t3 = SOWING
	ga.event.t4 = SOWING
	ga.event.t5 = GROWING
	ga.event.t6 = SOWING
	ga.event.t7 = GROWING
	
	}
}

run initGame for 8 Time, 3 Flower, 3 Tool, 1 Gardener,  6 Int, 3 GameState, 4 GameEvent
	
