# Juggle
Problem

Each team will attempt to complete a juggling circuit consisting of several tricks. Each circuit emphasizes different aspects of juggling, requiring hand to eye coordination (H), endurance (E) and pizzazz (P) in various amounts for successful completion. Each juggler has these abilities in various amounts as well. How good a match they are for a circuit is determined by the dot product of the juggler's and the circuit's H, E, and P values. The higher the result, the better the match.

Each participant will be on exactly one team and there will be a distinct circuit for each team to attempt. Each participant will rank in order of preference their top X circuits. Since we would like the audiences to enjoy the performances as much as possible, when assigning jugglers to circuits we also want to consider how well their skills match up to the circuit. In fact we want to match jugglers to circuits such that no juggler could switch to a circuit that they prefer more than the one they are assigned to and be a better fit for that circuit than one of the other jugglers assigned to it.

To help us create the juggler/circuit assignments write a program in a language of your choice that takes as input a file of circuits and jugglers and outputs a file of circuits and juggler assignments. The number of jugglers assigned to a circuit should be the number of jugglers divided by the number of circuits. Assume that the number of circuits and jugglers will be such that each circuit will have the same number of jugglers with no remainder.
