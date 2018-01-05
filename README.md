### READ ME / INSTRUCTIONS

Rules: You should be familiar with it but if not, check Wiki

Setup: 
> Quick game has 9 tiles per player, so the game goes by much faster. Normal has 7 tiles per player
> Casual mode introduces a custom gameplay style where you can see all the options for a word input as you type it. This makes for much faster and easier gameplay on a mac, but admittedly less challenging. Although this has been educational for me, discovering words I didn't know where words. Hard mode hides the positions from you, forcing you to choose your locations yourself. 
> you can only add up to 6 players. I believe this is the official rule.
>> Note: if you're not sure if it is a word or not, YES it is, check the supplied dictionary (words_scrabble.txt). The dictionary checker does not lie :P

Gameplay:
> inserting words
click the "type a word" textfield to begin typing your word. Either on casual or on hard mode, hover over the starting point of your word (the word will only ever go right or down). On hard mode, click to see the input, and click again to confirm, or move the mouse to hide. On either mode, if it can go either right or down, click the start point (corner point), then right or down arrows will show up. Click either to confirm the direction, or mouse away to hide and pick another spot.
>> Note: my gameplay includes blank tiles, which are not required by the project. If any glitch occurs due to blank tiles (I dont think any will occur), assume I never implemented blank tiles and grade accordingly. I just want to be safe while also trying out blank implementation
> shuffling/swapping tiles in rack
click "swap tiles" button on left, then type what you want to swap. you can type anything but only the ones in your rack will be swapped.
> buying special tiles
click "buy specials" button on left to enter the "shop" menu. click on the icon of the special tile you wish to buy. Your score will automatically be deducted the price. Nothing will happen if you dont have enough points
> ending turn
click "end turn" on left
> ending game
if all players agree to end the game, click "end game" on left. The winner(s) will be announced

Other:
> have fun! it's really fun to play casual with friends, esp when you dont know the word

>> ** IF ANYTHING GOES WRONG OR DOES NOT LOOK RIGHT, CONSULT THIS PROOF THAT IT WORKS ON MY COMPUTER: https://youtu.be/q_G_itBxGhs
AND IMMEDIATELY CONTACT ME

>> ** new in 10/31 update (second late day) "Halloween Update"
> reorganized some methods, parameters and if-statememnt checks in the getMoves() process, now the game should be absolutely bug-free in terms of word position finding and placement.
> added 50 pt bonus to moves that use all 7/9 of the tiles in the player rack (as per official rules on wiki)
> minor fixes and documentation updates
> Changed "Hard" mode title to "Classic" mode

>> ** new in 10/31 update #2
> turns out I had the scoring rules all wrong, updated the rules for scoring so now everything should be accurately scored as well as accurately placed!
> fixed the 50 pt bonus so that it isn't awarded every time a user has less than the tile rack size, after the tile bag has been emptied
> added a new scoring rule, according to official rules, as defined below:

"Final Scores in Scrabble

Who “goes out” also has a big affect on the score. Eventually, the letter tiles will run out. When this happens, you will have a dwindling number of letter tiles on your rack. When this happens, the first person to get rid of all the letters on their rack on their turn “goes out”. The scoring is not yet finished, though.

Every player with letters should add up the point values of those letters. These players should subtract that letter amount from their score.

Once this is done, the point value for all those letters should also be added up collectively and added to the score of the person who “went out” or got rid of all their letter tiles first. In this way, the winner of a Scrabble game is often determined by who goes out first."