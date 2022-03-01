/** 
* @file Assig6.h
* Module 6 Assignment: "Timed "Build" Game"
* @author Aisha Lalli
* @author Fadl Ghaddar
* @author Benjamin Mona
* @author Abraham Borg
*
* This program will simulate a card game between a computer player 
* and a human player. The methods from earlier projects for cards games \
* are being used but the project code is arranged using the MVC design pattern. 
* In this project we also learned how to use the basics of multi-threading to
* incorporate a timer which runs separately from the main game.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

public class Assig6
{
   public static void main(String[] args)
   {
      Model model  = new Model(); 
      View view = new View();
      Controller controller = new Controller(model, view);
      controller.SetJFrames();
      controller.computerTurn();
   } // main method
} // Assig6 Main class


class Model
{  
   CardGameOutline SuitMatchGame;
   static final int NUM_CARDS_PER_HAND = 7;
   static final int NUM_PLAYERS = 2;
   int computerIndex;
   int stackIndex;
   int totalSeconds = 0;
   int totalMinutes = 0; 
   String totalSecondsString;
   int PAUSE = 1000;
   Hand [] playAreaHand;
   static final int NUM_OF_STACKS = 3;
   String declaredWinner;
   boolean compDidntPlay = false;
   boolean playerDidntPlay = false;
   
   
   /** MODEL()
    * 
    */
   Model()
   {
      int numPacksPerDeck = 1, numJokersPerPack = 2, numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;
      
      SuitMatchGame = new CardGameOutline(numPacksPerDeck, numJokersPerPack, 
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      playAreaHand = new Hand[3];
      
      SuitMatchGame.deal();
      GUICard.loadCardIcons();
   } // Model default constructor
   

   /** PLAYERTURN(int)
    * If the player can play a card, it will return true
    * Otherwise, if the player can't play a card, it will return false
    * anyEmptyStacks(playerCard) will add a card to an empty stack
    * canPlayerCardBePlaced will find out if a card can be added to any 
    * of the 3 stacks, and then add that card
    */
   public boolean playerTurn(int userIndex)
   {
      Card playerCard = new Card(
            SuitMatchGame.getHand(1).inspectCard(userIndex));
      
      if (anyEmptyStacks(playerCard))
         return true;
      else if (canPlayerCardBePlaced(playerCard))
         return true;
      else
         return false;
   } // playerTurn method
   
   
   /** COMPUTERTURN()
    * If the computer can play a card, it will return true
    * Otherwise, if the computer can't play a card, it will return false
    * anyEmptyStacks(computerCard) will add a card to an empty stack
    * canComputerCardBePlaced will find out if a card can be added 
    * to any of the 3 stacks, and then add that card
    */
   public boolean computerTurn()
   {
      computerIndex = SuitMatchGame.getHand(0).getNumCards() - 1;
      Card computerCard = new Card(
            SuitMatchGame.getHand(0).inspectCard(computerIndex));
      
      if (anyEmptyStacks(computerCard))
         return true;
      else if (canComputerCardBePlaced())
         return true;
      else
         return false;
   } // computerTurn method
   
   
   /** ANYEMPTYSTACKS(Card) 
    * If there are any empty stacks, then this function will return true
    * Before returning true, it will call a function that adds 
    * the card to the empty stack. If all 3 stacks have been created, 
    * then this function will return false
    */
   public boolean anyEmptyStacks(Card aCard)
   {
      if (playAreaHand[0] == null || playAreaHand[1] == null 
            || playAreaHand[2] == null)
      {
         addToEmptyStack(aCard);
         return true;
      }
      else
         return false;
   } // anyEmptyStacks method
   
   
   /** ADDTOEMPTYSTACK(Card)
    * Function that receives a card, and adds that card to an empty stack
    * @param aCard
    */
   public void addToEmptyStack(Card aCard)
   {
      if (playAreaHand[0] == null)
      {
         stackIndex = 0;
         playAreaHand[stackIndex] = new Hand();
         playAreaHand[stackIndex].takeCard(aCard);
      }
      else if (playAreaHand[1] == null)
      {
         stackIndex = 1;
         playAreaHand[stackIndex] = new Hand();
         playAreaHand[stackIndex].takeCard(aCard);
      }
      else
      {
         stackIndex = 2;
         playAreaHand[stackIndex] = new Hand();
         playAreaHand[stackIndex].takeCard(aCard);
      }
   } // addToEmptyStack method
   
   
   /** CANCOMPUTERCARDBEPLACED()
    * If the computer can play a card, then this function will return true
    * Otherwise, if the computer can't play a card, it will return false 
    */
   public boolean canComputerCardBePlaced()
   {
      for (int i = 0; i < SuitMatchGame.getHand(0).getNumCards(); i++)
      {
         Card computerCard = new Card(SuitMatchGame.getHand(0).inspectCard(i));
         for (int j = 0; j < NUM_OF_STACKS; j++)
         {
            int topCardOnThisStack = playAreaHand[j].getNumCards() - 1;
            Card playAreaHandCard = new Card(
                  playAreaHand[j].inspectCard(topCardOnThisStack));
            
            int playedCardValue = GUICard.valueAsInt(playAreaHandCard);
            int computerCardValue = GUICard.valueAsInt(computerCard);
            if (computerCardValue - playedCardValue == 1 
                  || playedCardValue - computerCardValue == 1)
            {
               compDidntPlay = false;
               playerDidntPlay = false;
               stackIndex = j;
               computerIndex = i;
               playAreaHand[j].takeCard(computerCard);
               return true;
            }
         } // for
      } // for
      return false;
   } // canComputerCardBePlaced method
   
   
   /** CANPLAYERCARDBEPLACED(Card)
    * If the player plays a card that can be played, 
    * then this function will return true. Otherwise, if the player plays 
    * an incorrect card, it will return false 
    */
   public boolean canPlayerCardBePlaced(Card aCard)
   {
      for (int i = 0; i < SuitMatchGame.getHand(1).getNumCards(); i++)
      {
         for (int j = 0; j < NUM_OF_STACKS; j++)
         {
            int topCardOnThisStack = playAreaHand[j].getNumCards() - 1;
            Card playAreaHandCard = new Card(
                  playAreaHand[j].inspectCard(topCardOnThisStack));
            
            int playedCardValue = GUICard.valueAsInt(playAreaHandCard);
            int playerCardValue = GUICard.valueAsInt(aCard);
            if (playerCardValue - playedCardValue == 1 
                  || playedCardValue - playerCardValue == 1)
            {
               playerDidntPlay = false;
               compDidntPlay = false;
               stackIndex = j;
               playAreaHand[j].takeCard(aCard);
               return true;
            }
         } // for
      } // for
      return false;
   } // canPlayerCardBePlaced method
   
   
   /** COMPUTERCANNOTPLAY()
    * Function that is called if the computer cannot play a correct card
    */
   public void computerCannotPlay()
   {
      compDidntPlay = true;
      SuitMatchGame.getHand(0).addToCannotPlay();
      if (didBothSkipATurn())
         dealOneCardToAStack();
   } // computerCannotPlay method
   
   
   /** PLAYERCANNOTPLAY()
    * FUnction that is called if the player picks an incorrect card, or clicks "I cannot play"
    */
   public void playerCannotPlay()
   {
      playerDidntPlay = true;
      SuitMatchGame.getHand(1).addToCannotPlay();
      if (didBothSkipATurn())
         dealOneCardToAStack();
   } // playerCannotPlay method
   
   
   /** DIDBOTHSKIPATURN()
    * Function that assess whether the computer and the player both skipped a turn
    * @return
    */
   public boolean didBothSkipATurn()
   {
      if (compDidntPlay && playerDidntPlay)
         return true;
      else
         return false;
   } // didBothSkipATurn method
   
   
   /** DEALONECARDTOASTACK()
    * Deals a card to the stack
    */
   public void dealOneCardToAStack()
   {
      playerDidntPlay = false;
      compDidntPlay = false;
      
      if (SuitMatchGame.getNumCardsRemainingInDeck() > 0)
      {
         playAreaHand[0].takeCard(SuitMatchGame.getCardFromDeck());
      }
   } // dealOneCardToAStack method
   
   
   /** INCREMENTSECONDS()
    * Increment seconds
    */
   public synchronized void incrementSeconds()
   {
      if(totalSeconds == 60)
      {
         totalMinutes++;
         totalSeconds = 0;
      }
      
      if(totalSeconds <10)
      {
         totalSecondsString = (totalMinutes + " : 0"+ totalSeconds);
      }
      
      else
      {
         totalSecondsString = (totalMinutes + " : "+ totalSeconds);
      }
      
      totalSeconds++;
      doNothing(PAUSE);
   } // incrementSeconds()
   
   
   /** DONOTHING(int)
    * Function called by increment seconds
    * @param milliseconds
    */
   public void doNothing(int milliseconds)
   {
      try
      {
         Thread.sleep(milliseconds);
      }
      catch(InterruptedException e)
      {
         System.out.println("Unexpected Interupt");
         System.exit(0);
      }
   } // doNothing method
   
   
   /** COMPUTERPLAYSCARD()
    * lay the computer card from the computers hand
    */
   public void computerPlaysCard()
   {
      if (!compDidntPlay)
         SuitMatchGame.getHand(0).playCard(computerIndex);
   } // computerPlaysCard method
   
   
   /** COMPUTERTAKESCARDFROMDECK()
    * Computer hand takes a card from the deck
    */
   public void computerTakesCardFromDeck()
   {
      if (SuitMatchGame.getNumCardsRemainingInDeck() != 0 && !compDidntPlay)
         SuitMatchGame.getHand(0).takeCard(SuitMatchGame.getCardFromDeck());
   } // computerTakesCardFromDeck method
   
   
   /** PLAYERPLAYSCARD(int)
    * Play the player card from the players hand
    * @param userButtonIndex
    */
   public void playerPlaysCard(int userButtonIndex)
   {
      if (!playerDidntPlay)
         SuitMatchGame.getHand(1).playCard(userButtonIndex);
   } // playerPlaysCard method
   
   
   /** PLAYERTAKESCARDFROMDECK()
    * Player hand takes a card from the deck
    */
   public void playerTakesCardFromDeck()
   {
      if (SuitMatchGame.getNumCardsRemainingInDeck() != 0 && !playerDidntPlay)
         SuitMatchGame.getHand(1).takeCard(SuitMatchGame.getCardFromDeck());
   } // playerTakesCardFromDeck method
   
   
   /** CALCULATEWINNER()
   * this method will set the instance variable of the model class, 
   * declaredWinner to a value based on boolean, who won. 
   * This method is called when the deck runs out of cards
   */
   public void calculateWinner()
   {
      if (SuitMatchGame.getHand(0).getNumOfCannotPlays() 
            > SuitMatchGame.getHand(1).getNumOfCannotPlays())
      {
         declaredWinner = "Human Player Wins!";
      }
      else if (SuitMatchGame.getHand(0).getNumOfCannotPlays() 
            < SuitMatchGame.getHand(1).getNumOfCannotPlays())
      {
         declaredWinner = "Computer Player Wins!";
      }
      else
      {
         declaredWinner = "Tie!";
      } // if-else
   } // calculateWinner method
} // Model class


class View extends JFrame
{
   static final int NUM_CARDS_PER_HAND = 7;
   static final int NUM_PLAYERS = 2;
   static final int NUM_OF_STACKS = 3;
   static int counter = 0;
   CardTable myCardTable;
   JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   JButton[] humanButtons = new JButton[NUM_CARDS_PER_HAND];
   JButton computerButton;
   JButton iCannotPlay;
   JLabel[] playedCardLabels = new JLabel[NUM_OF_STACKS];
   JFrame winFrame;
   JLabel winFrameLabel;
   JLabel timeLabel;
   JLabel declaredWinner, gameDeckText, numOfCannotPlay1, numOfCannotPlay2;


   /** SETGAMETABLE(Model)
    * Function that sets up the game table
    * @param aModel
    */
   public void setGameTable(Model aModel)
   {
      myCardTable  = 
            new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 800);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myCardTable.setVisible(true);
   } // setGameTable method
   
   
   /** ADDLABELSANDBUTTONS(Model, ActionListener)
    * Add labels and buttons to the card table
    * @param aModel
    * @param buttonEar
    */
   public void addLabelsAndButtons(Model aModel, ActionListener buttonEar)
   {
      for (int i = 0; i < NUM_CARDS_PER_HAND + 1; i++)
      {
         if (i == NUM_CARDS_PER_HAND)
         {
            computerButton = new JButton("I cannot play");
            computerButton.setActionCommand(Integer.toString(i));
            computerButton.addActionListener(buttonEar);
            computerButton.setVisible(false);
            myCardTable.pnlComputerHand.add(computerButton);
            iCannotPlay =  new JButton("I cannot play");
            iCannotPlay.setActionCommand(Integer.toString(i + 1));
            iCannotPlay.addActionListener(buttonEar);
            myCardTable.pnlHumanHand.add(iCannotPlay);
         }
         else
         {
            computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
            Card tempCard = new Card(
                  aModel.SuitMatchGame.getHand(1).inspectCard(i));
            
            humanButtons[i] =  new JButton(GUICard.getIcon(tempCard));
            humanButtons[i].setActionCommand(Integer.toString(i));
            humanButtons[i].addActionListener(buttonEar);
            myCardTable.pnlComputerHand.add(computerLabels[i]);
            myCardTable.pnlHumanHand.add(humanButtons[i]);
         } // if-else
      } // for
      
      myCardTable.pnlPlayArea.setLayout(new GridLayout(1,3));
      JPanel firstCardSpot = new JPanel();
      JPanel secondCardSpot = new JPanel();
      JPanel thirdCardSpot = new JPanel();
      playedCardLabels[0] = new JLabel();
      playedCardLabels[1] = new JLabel();
      playedCardLabels[2] = new JLabel();
      firstCardSpot.add(playedCardLabels[0]);
      secondCardSpot.add(playedCardLabels[1]);
      thirdCardSpot.add(playedCardLabels[2]);
      myCardTable.pnlPlayArea.add(firstCardSpot);
      myCardTable.pnlPlayArea.add(secondCardSpot);
      myCardTable.pnlPlayArea.add(thirdCardSpot);
      myCardTable.setVisible(true);
   } // addLabelsAndButtons method
   
   
   /** ADDTIMERLABELSANDBUTTON(ActionListener)
    * dd timers labels and timer buttons to card table
    * @param buttonEar
    */
   public void addTimerLabelsAndButton(ActionListener buttonEar) 
   {
      //timer
      myCardTable.pnlTimer.setLayout(new GridLayout(1,4));
      JPanel pnlTimerLeft = new JPanel();
      myCardTable.pnlTimer.add(pnlTimerLeft);
      JPanel pnlTimerRight = new JPanel();
      myCardTable.pnlTimer.add(pnlTimerRight);
      JPanel pnlTimerRight2 = new JPanel();
      myCardTable.pnlTimer.add(pnlTimerRight2);
      JPanel pnlTimerRight3 = new JPanel();
      myCardTable.pnlTimer.add(pnlTimerRight3);
      
      Border blackLineTimer = 
            BorderFactory.createTitledBorder("Timer");
      pnlTimerLeft.setBorder(blackLineTimer);
      
      
      timeLabel = new JLabel();
      pnlTimerLeft.add(timeLabel);
      JButton strt = new JButton("Start/Stop");
      strt.addActionListener(buttonEar);
      pnlTimerLeft.add(strt);
   } // addTimerLabelsAndButton method
   
   
   /** UPDATECOMPUTERHAND(Model)
    * pdate the JFrame to reflect the computers hand
    * @param aModel
    */
   public void updateComputerHand(Model aModel)
   {
      computerLabels[aModel.computerIndex].setVisible(false);      
   } // updateComputerHand method
   
   
   /** UPDATECARDTABLE(Model)
    * Update the cards in the stack
    * @param aModel
    */
   public void updateCardTable(Model aModel)
   {          
      for (int i = 0; i < NUM_OF_STACKS; i++)
      {
         if (aModel.playAreaHand[i] != null)
         {
            int topStackIndex = (aModel.playAreaHand[i].getNumCards()) - 1;
            Card topStackCard = 
                  aModel.playAreaHand[i].inspectCard(topStackIndex);
            
            playedCardLabels[i].setIcon(GUICard.getIcon(topStackCard));
         } 
      } // for
   } // updateCardTable method
   
   
   /** REARRANGEBUTTONS(Model)
    * Rearrange the buttons based on the cards in the players hand
    * @param aModel
    */
   public void reArrangeButtons(Model aModel)
   {
      for (int i = 0; i < aModel.SuitMatchGame.getHand(1).getNumCards(); i++)
      {
         humanButtons[i].setVisible(false);
         computerLabels[i].setVisible(false);
      }
      
      // Sort the buttons to match the order of the cards that are in the hand
      for (int i = 0; i < 
            aModel.SuitMatchGame.getHand(1).getNumCards() + 1; i++)
      {
         if (i < aModel.SuitMatchGame.getHand(1).getNumCards())
         {
            Card thisCard = 
                  new Card(aModel.SuitMatchGame.getHand(1).inspectCard(i));
            
            humanButtons[i].setIcon(GUICard.getIcon(thisCard));
            humanButtons[i].setVisible(true);
            computerLabels[i].setVisible(true);
            myCardTable.pnlComputerHand.add(computerLabels[i]);
            myCardTable.pnlHumanHand.add(humanButtons[i]);
         }
         else
         {
            myCardTable.pnlHumanHand.add(iCannotPlay);
            myCardTable.pnlComputerHand.add(computerButton);
         } //  if-else
      } // for
   } // reArrangeButtons method
   
      
   /** CREATEWINFRAME(Model)
   * This method will create a separate JFrame which will show the 
   * current number of cards in deck, the number of times that each player 
   * has not been able to play a card, and at the end of the game the
   * declared winner. 
   */
   public void createWinFrame(Model aModel)
   {
      JFrame gameWinFrame = new JFrame();
      gameWinFrame.setLayout(new GridLayout(4, 1)); 
      gameWinFrame.setSize(400, 400);
      gameWinFrame.setTitle("Game Status & Results");
      gameWinFrame.setLocationRelativeTo(null);
      gameWinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      gameDeckText = 
            new JLabel("Cards left in Deck : " + String.valueOf(
                  aModel.SuitMatchGame.getNumCardsRemainingInDeck()), 
                  JLabel.CENTER);
      
      numOfCannotPlay1 = 
            new JLabel("The computer hasn't been able to play a card: " 
                  + aModel.SuitMatchGame.getHand(0).getNumOfCannotPlays() 
                  + " times.", JLabel.LEFT);
      
      numOfCannotPlay2 = 
            new JLabel("The human hasn't been able to play a card: " 
                  + aModel.SuitMatchGame.getHand(1).getNumOfCannotPlays() 
                  + " times.", JLabel.LEFT);
      
      // the declared winner will be shown at the end of the game.
      declaredWinner = new JLabel(); 
      
      gameWinFrame.add(gameDeckText);
      gameWinFrame.add(numOfCannotPlay1);
      gameWinFrame.add(numOfCannotPlay2);
      gameWinFrame.add(declaredWinner);
      gameWinFrame.setVisible(true);
   } // createWinFrame method
   
   
   /** UPDATEWINFRAME(Model)
   * Call this method after the computer takes a turn, and also after a 
   * player takes a turn, and also after the click of the "I can't play" 
   * button.
   */
   public void updateWinFrame(Model aModel)
   {
      if ( aModel.SuitMatchGame.getNumCardsRemainingInDeck() <= 0) 
      {
         declaredWinner.setText(aModel.declaredWinner);
      }
      // update JFrame
      gameDeckText.setText("Cards left in Deck : " 
            + String.valueOf(
                  aModel.SuitMatchGame.getNumCardsRemainingInDeck()));
      
      numOfCannotPlay1.setText("The computer hasn't been able to play a card: " 
                  + aModel.SuitMatchGame.getHand(0).getNumOfCannotPlays() 
                  + " times.");
      
      numOfCannotPlay2.setText("The human hasn't been able to play a card: " 
            + aModel.SuitMatchGame.getHand(1).getNumOfCannotPlays() 
            + " times.");
      
   } // updateWinFrame method
   
   
   /** DISABLEBUTTONS(Model)
    * 
    * @param aModel
    */
   public void disableButtons(Model aModel)
   {
      computerButton.setEnabled(false);
      iCannotPlay.setEnabled(false);
      for (int i = 0; i < aModel.SuitMatchGame.getHand(1).getNumCards(); i++)
         humanButtons[i].setEnabled(false);
   } // disableButtons(Model)
} // View class


class Controller
{
   private Model model;
   private View view;
   int userButtonIndex;
   int totalSeconds = 0;
   int totalMinutes = 0;
   boolean userButtonTimer = false;
   String totalSecondsString;
   Timer timerThread;
   
   
   /** CONTROLLER(Model, View)
    * 
    * @param aModel
    * @param aView
    */
   Controller(Model aModel, View aView)
   {
      this.model = aModel;
      this.view = aView;
   } // Controller constructor
   
   
   /** SETJFRAMES()
    * Calls function within the view
    * The view functions that are called will set the JFrames up
    */
   public void SetJFrames()
   {
      view.setGameTable(model);
      view.addLabelsAndButtons(model, new ChosenCardButton());
      view.addTimerLabelsAndButton(new ChosenCardButton());
      view.createWinFrame(model); // this will create a separate JFrame
   } // SetJFrames method
   
   
   /** COMPUTERTURN()
    * Function that controls the computers play pattern
    * If the computer can take a turn, then it will play a card and update the table
    * Otherwise, it will indicate that it can't play
    * The last if statement checks to see whether the game is over
    */
   public void computerTurn()
   {
      if (model.computerTurn())
      {
         view.updateComputerHand(model);
         view.updateCardTable(model);
         updateComputerCard();
      }
      else
         model.computerCannotPlay();
      
      if (model.SuitMatchGame.getNumCardsRemainingInDeck() <= 0)
      {
         model.calculateWinner();
         view.updateWinFrame(model);
         view.disableButtons(model);
      }
      
      view.updateWinFrame(model);// call to update win frame.
   } // computerTurn method
   
   
   /** TIMER CLASS
    * helper class
    */
   private class Timer extends Thread
   {
      public void run()
      {
         while(userButtonTimer)
         {
            view.timeLabel.setText(model.totalSecondsString);
            model.incrementSeconds();
         }
         
         try
         {
            timerThread.join();
         } 
         catch (InterruptedException e)
         {
            e.printStackTrace();
         } // try-catch
      } // run method
   } // Timer class
   
   
   /** CHOSENCARDBUTTON CLASS
    * helper class
    * Action commands for whenever a button is clicked
    */
   class ChosenCardButton implements ActionListener 
   {
      public void actionPerformed(ActionEvent e)
      {
         view.computerButton.setVisible(false);

         // actionCommands for the the timer
         if(e.getActionCommand().equals("Start/Stop"))
         {
            if(!userButtonTimer) 
            {
               userButtonTimer = true;
            }
            else
            {
               userButtonTimer = false;
            } // if-else
            
            timerThread = new Timer();
            timerThread.start();
         } // if
         
         // actionCommands for I cannot play
         else if (e.getActionCommand().equals("7") 
               || e.getActionCommand().equals("8"))
         {
            model.calculateWinner();
            view.updateWinFrame(model);
            if(e.getActionCommand().equals("7"))
            {
               model.computerCannotPlay();
               view.updateWinFrame(model);
               view.reArrangeButtons(model);
            }
            else
            {
               model.playerCannotPlay();
               view.updateWinFrame(model);
               view.reArrangeButtons(model);
               computerTurn();
               updateComputerCard();
            } // if-else
            
            if (model.didBothSkipATurn())
            {
               model.dealOneCardToAStack();
               view.updateCardTable(model);
               view.updateWinFrame(model);            
            }
         } // else if, actionCommands for I cannot play
         
         // actionButtons for the human card buttons
         else
         {
            if(e.getActionCommand().equals("0"))
            {
               if (model.playerTurn(userButtonIndex = 0))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();
               }
            }
            
            else if (e.getActionCommand().equals("1"))
            {
               if (model.playerTurn(userButtonIndex = 1))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();
               }
            }
            
            else if(e.getActionCommand().equals("2"))
            {
               if (model.playerTurn(userButtonIndex = 2))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();               
               }
            }
            
            else if(e.getActionCommand().equals("3"))
            {
               if (model.playerTurn(userButtonIndex = 3))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();               
               }
            }
            
            else if(e.getActionCommand().equals("4"))
            {
               if (model.playerTurn(userButtonIndex = 4))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();               
               }
            }
            
            else if(e.getActionCommand().equals("5"))
            {
               if (model.playerTurn(userButtonIndex = 5))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();               
               }
            }
            
            else if(e.getActionCommand().equals("6"))
            {
               if (model.playerTurn(userButtonIndex = 6))
               {
                  view.updateCardTable(model);
               }
               else
               {
                  model.playerCannotPlay();               
               }
            }
            
            view.updateWinFrame(model);                 
            updatePlayerCards();
            if (model.SuitMatchGame.getNumCardsRemainingInDeck() > 0)
                computerTurn();
            else
            {
               model.calculateWinner();
               view.updateWinFrame(model);
               view.disableButtons(model);
            }
         } // else, actionButtons for the human card buttons
      } // actionPerformed method
   } // ChosenCardButton class
   
   
   /** UPDATECOMPUTERCARD()
    * Function that updates the computers cards
    */
   public void updateComputerCard()
   {
      model.computerPlaysCard();
      model.computerTakesCardFromDeck();
   } // updateComputerCard method
   
   
   /** UPDATEPLAYERCARDS()
    * Function that updates the players cards
    */
   public void updatePlayerCards()
   {
      model.playerPlaysCard(userButtonIndex);
      model.playerTakesCardFromDeck();
      view.reArrangeButtons(model);
   } // updatePlayerCards method
} // Controller class


//class CardGameOutline  ----------------------------------------------------
class CardGameOutline
{
 
 private static final int MAX_PLAYERS = 50;

 private int numPlayers;
 private int numPacks;            // # standard 52-card packs per deck
                               // ignoring jokers or unused cards
 private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
 private int numUnusedCardsPerPack;  // # cards removed from each pack
 private int numCardsPerHand;        // # cards to deal each player
 private Deck deck;               // holds the initial full deck and gets
                              
 private Hand[] hand;             // one Hand for each player
 private Card[] unusedCardsPerPack;  // an array holding the cards not used
                                    

 public CardGameOutline( int numPacks, int numJokersPerPack, 
       int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
       int numPlayers, int numCardsPerHand)
 {
    int k;

    // filter bad values
    if (numPacks < 1 || numPacks > 6)
       numPacks = 1;
    if (numJokersPerPack < 0 || numJokersPerPack > 4)
       numJokersPerPack = 0;
    if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
       numUnusedCardsPerPack = 0;
    if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
       numPlayers = 4;
    // one of many ways to assure at least one full deal to all players
    if  (numCardsPerHand < 1 
          ||numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
          / numPlayers ) numCardsPerHand = 
          numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

    // allocate
    this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
    this.hand = new Hand[numPlayers];
    for (k = 0; k < numPlayers; k++)
       this.hand[k] = new Hand();
    deck = new Deck(numPacks);

    // assign to members
    this.numPacks = numPacks;
    this.numJokersPerPack = numJokersPerPack;
    this.numUnusedCardsPerPack = numUnusedCardsPerPack;
    this.numPlayers = numPlayers;
    this.numCardsPerHand = numCardsPerHand;
    for (k = 0; k < numUnusedCardsPerPack; k++)
    {
       this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
    }

    // prepare deck and shuffle
    newGame();
    }// CardGameOutline()

  // constructor overload/default for game like bridge
 public CardGameOutline()
 {
   this(1, 0, 0, null, 4, 13);
 }

 public Hand getHand(int k)
 {
   // hands start from 0 like arrays
   // on error return automatic empty hand
   if (k < 0 || k >= numPlayers)      
   {
       return new Hand();
   }// if
   return hand[k];
 } // getHand

  public Card getCardFromDeck() 
  {
     return deck.dealCard(); 
  }

  public int getNumCardsRemainingInDeck() 
  { 
     return deck.getNumCards();
  }

  public void newGame()
  {
    int k, j;

    // clear the hands
    for (k = 0; k < numPlayers; k++)
       hand[k].resetHand();

    // restock the deck
    deck.init(numPacks);

    // remove unused cards
    for (k = 0; k < numUnusedCardsPerPack; k++) 
       deck.removeCard( unusedCardsPerPack[k] );

    // add jokers
    for (k = 0; k < numPacks; k++)
    {
       for ( j = 0; j < numJokersPerPack; j++)
       {
          deck.addCard( new Card('X', Card.Suit.values()[j]) );
       }
    }

    // shuffle the cards
    deck.shuffle();
  } // newGame

  public boolean deal()
  {
    // returns false if not enough cards, but deals what it can
    int k, j;
    boolean enoughCards;

    // clear all hands
    for (j = 0; j < numPlayers; j++)
       hand[j].resetHand();

    enoughCards = true;
    for (k = 0; k < numCardsPerHand && enoughCards ; k++)
    {
       for (j = 0; j < numPlayers; j++)
       {
          if (deck.getNumCards() > 0) 
             hand[j].takeCard( deck.dealCard() );
          else
          {
             enoughCards = false;
             break;
          }// else
       }// for
    }// for
    return enoughCards;
  }// deal

  public void sortHands()
  {
     int k;

     for (k = 0; k < numPlayers; k++)
     {
        hand[k].sort();
     }// for
  }// sortHands

  public Card playCard(int playerIndex, int cardIndex)
  {
    // returns bad card if either argument is bad
    if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
        cardIndex < 0 || cardIndex > numCardsPerHand - 1)
    {
       //Creates a card that does not work
       return new Card('M', Card.Suit.spades);      
    }
    // return the card played
    return hand[playerIndex].playCard(cardIndex);
  }// playCard


  public boolean takeCard(int playerIndex)
  {
     // returns false if either argument is bad
    if (playerIndex < 0 || playerIndex > numPlayers - 1)
    {
       return false;
    }// if
    
    // Are there enough Cards?
    if (deck.getNumCards() <= 0)
    {
     return false;
    }// if

  return hand[playerIndex].takeCard(deck.dealCard());
  }// takeCard

}// CardGameOutline class


//CardTable class that extends JFrame
//Controls the positioning of the panels and cards of the GUI
class CardTable extends JFrame
{
 static int MAX_CARDS_PER_HAND = 56;
 static int MAX_PLAYERS = 2;
 
 private int numCardsPerHands;
 private int numPlayers;
 
 public JPanel pnlComputerHand; 
 public JPanel pnlHumanHand;
 public JPanel pnlPlayArea;
 public JPanel pnlTimer;

 public CardTable()
 {
    this("Default", 7, 2);
 }
 
 public CardTable(String title, int numCardsPerHand, int numPlayers)
 {
    super();
    
    if((numPlayers > MAX_PLAYERS) || (numCardsPerHand > MAX_CARDS_PER_HAND)  
          || (numPlayers < 0) || (numCardsPerHand < 0))
    {
       title = "Default view due to Error Input";
       numCardsPerHand = 7;
       numPlayers = 2;
    }
    
    setTitle(title);
    setLayout(new GridLayout(numPlayers +2 ,1));
    pnlComputerHand = new JPanel();
    Border blacklineComputer = 
          BorderFactory.createTitledBorder("Computer Hand");
    pnlComputerHand.setBorder(blacklineComputer);
    add(pnlComputerHand);
    pnlPlayArea = new JPanel();
    Border blacklinePlayArea = 
          BorderFactory.createTitledBorder("Playing Area");
    pnlPlayArea.setBorder(blacklinePlayArea);
    add(pnlPlayArea);
    pnlHumanHand = new JPanel();
    Border blacklineHuman = BorderFactory.createTitledBorder("Human Hand");
    pnlHumanHand.setBorder(blacklineHuman);
    add(pnlHumanHand);
    
    pnlTimer = new JPanel();
    add(pnlTimer);
    
 } //CardTable
 
 // Accessors below
 public int getNumCardsPerHand()
 {
    return numCardsPerHands;
 }
 
 public int numPlayers()
 {
    return numPlayers;
 }
 
}//CardTable Class


//Create a new GUICard class
//Manages the reading and building of the card image Icons
class GUICard
{
// 14 = A thru K + joker
   private static Icon[][] iconCards = new ImageIcon[14][4]; 
   private static Icon iconBack;
   static boolean iconsLoaded = false;
 
   static void loadCardIcons()
   {
      if (!iconsLoaded)
      {
         String stringValue [] = new String[14];
         String stringSuit [] = new String[4];
       
         for (int valueCounter = 0; valueCounter < 14; valueCounter++)
         {
            stringValue[valueCounter] = 
                  "images/" + turnIntIntoCardValue(valueCounter);
         }
          
         for (int suitCounter = 0; suitCounter < 4; suitCounter++)
         {
            stringSuit[suitCounter] = turnIntIntoCardSuit(suitCounter) + ".gif";
         }
       
         for (int valueCounter = 0; valueCounter < 14; valueCounter++)
         {
            for (int suitCounter = 0; suitCounter < 4; suitCounter++)
            {
               iconCards[valueCounter][suitCounter] = 
                     new ImageIcon(stringValue[valueCounter] + 
                           stringSuit[suitCounter]);
            } // for
         } // for
   
         iconBack = new ImageIcon("images/BK.gif");
         iconsLoaded = true;
      }
      else
         return;
   }//loadCardIcons()
 
   static public Icon getIcon(Card card)
   {
      loadCardIcons();
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }
   
 
   //method required for getIcon = gets int for card value
   public static int valueAsInt(Card card)
   {
      switch (card.getValue())
      {
         case 'A':
            return 0;
         case '2':
            return 1;
         case '3':
            return 2;
         case '4':
            return 3;
         case '5':
            return 4;
         case '6':
            return 5;
         case '7':
            return 6;
         case '8':
            return 7;
         case '9':
            return 8;
         case 'T':
            return 9;
         case 'J':
            return 10;
         case 'Q':
            return 11;
         case 'K':
            return 12;
         default:
            return 13;
      }
   } // valueAsInt method
 
   
   //method required for getIcon = gets int for card suit
   public static int suitAsInt(Card card)
   {
      if (card.getSuit() == Card.Suit.clubs)
         return 0;
      else if (card.getSuit() == Card.Suit.diamonds)
         return 1;
      else if (card.getSuit() == Card.Suit.hearts)
         return 2;
      else
         return 3;
   } // suitAsInt method
 
   static public Icon getBackCardIcon() 
   {
      return iconBack;
   }
 
   static String turnIntIntoCardValue(int k)
   {
      //System.out.println(k);
      if (k == 0)
         return "A";
      else if (k == 1)
         return "2";
      else if (k == 2)
         return "3";
      else if (k == 3)
         return "4";
      else if (k == 4)
         return "5";
      else if (k == 5)
         return "6";
      else if (k == 6)
         return "7";
      else if (k == 7)
         return "8";
      else if (k == 8)
         return "9";
      else if (k == 9)
         return "T";
      else if (k == 10)
         return "J";
      else if (k == 11)
         return "Q";
      else if (k == 12)
         return "K";
      else
         return "X";
 } // turnIntIntoCardValue
 
   static String turnIntIntoCardSuit(int j)
   {
      if (j == Card.Suit.clubs.ordinal())
      {
         return "C";
      }
    
      else if (j == Card.Suit.diamonds.ordinal())
      {
         return "D";
      }
    
      else if (j == Card.Suit.hearts.ordinal()) 
      {
         return "H";
      }
    
      else
         return "S";
       
   } // turnIntIntoCardSuit 
}//class GUICard


class Card
{
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   };
 
   private char value;
   private Suit suit;
   private Boolean errorFlag;
 
   // put the order of the card values in this array with the smallest
   // first, include 'X' for a joker.
   public static char[] valueRanks = {'A', '2', '3', '4', '5', '6',
         '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
 
 
   /** CARD()
    * 
    */
   public Card()
   {
      set('A', Suit.spades);
      valueRanks = new char[14];
   } // Card()
 
 
   /** CARD(Card)
    * 
    * @param aCard
    */
   public Card(Card aCard)   
   {  
      valueRanks = new char[14]; 
      set(aCard.value, aCard.suit);
   } // Card(Card)
 
 
   /** CARD(char, Suit)
    * 
    * @param aValue
    * @param aSuit
    */
   public Card(char aValue, Suit aSuit)
   {
      valueRanks = new char[14];
      set(aValue, aSuit);
   } // Card(char, Suit)
 
 
   /** ARRAYSORT(Card[], int)
    * This method will sort the incoming array of cards using a 
    * bubble sort routine. 
    * This method will sort the Cards in ascending order
    * 
    * @pre: cardArray is an array of arraySize nontrivial cards.
    * @post : cardArray is sorted in ascending order.
    * @param cardArray : this is the array that will be sorted.
    * @param arraySize
    */
   public static void arraySort(Card[] cardArray, int arraySize)
   {
      boolean isSorted = false; // false when swaps occur.
    
      for (int pass = 1; (pass < arraySize) && (! isSorted); pass++)
      {
         isSorted = true; // assume sorted
         for (int index = 0; index < arraySize - pass; index++)
         {
            int nextIndex = index + 1;
            if (cardArray[index].isGreater(cardArray[nextIndex]))
            {
               // exchange
               swapCards(cardArray[index], cardArray[nextIndex]);
               isSorted = false;
            } // if
         } // for
      }// for
   } // arraySort(Card[], int)
 
 
   /** ISGREATER(Card)
    * This is a helper method for the arraySort method.
    * This method will take a Card and compare the value to the calling card
    * object.
    * 
    * @param aCard
    * @return boolean : false means the calling Card is NOT greater than aCard
    */
   private boolean isGreater(Card aCard)
   {
      int callingCardValue = 0;
      int aCardValue = 0;
    
      if (this.equals(aCard))
      {
         return false; // same value means false.
      }
    
      // the first 2 for loops check which card has a higher value
      for (char value : valueRanks)
      {
         callingCardValue++;
         if (this.getValue() == value)
         {
            break;
         }
      }
    
      for(char value : valueRanks)
      {
         aCardValue++;
         if (aCard.getValue() == value)
         {
            break;
         }
      }
    
      if (callingCardValue > aCardValue)
      {
         return true;
      }
    
      // the ordinal method returns the position of the 
      // calling value in the list of enum values. The first position 
      // in the enum list is 0. So the spades would be 3.
      if (this.getSuit().ordinal() > aCard.getSuit().ordinal())
      {
         return true;
      }
    
      // at this point we know that the calling card
      // does not have a higher value, and also does not have a higher suit.
      return false;
   } // isGreater(Card) helper method.
 
 
   /** SWAPCARDS(Card, Card)
    * This is a helper method for the arraySort method.
    * @param card1
    * @param card2
    */
   private static void swapCards(Card card1, Card card2)
   {
      Card tempCard = new Card();
      tempCard = card2;
      card2 = card1; // swapped values so that card 2 contains the higher val.
      card1 = tempCard;
   } // swapCards(Card, Card)
 
 
   /** TOSTRING()
    * 
    */
   public String toString()
   {
      if (errorFlag == true)
      {
         return "** illegal **";
      }  
      else
      {
         return value + " of " + suit;
      }
    
   } // toString()
 
 
   /** SET(char, Suit)
    * 
    * @param aValue
    * @param aSuit
    * @return
    */
   public boolean set(char aValue, Suit aSuit)
   {  
      if (isValid(aValue, aSuit))
      {
         suit = aSuit;
         value = aValue;
       
         // when a card is corrupt, the errorFlag will be set to true
         // so if the card is ok, the errorFlag will be set to false.
         errorFlag = false;
       
         // the set method returns true because the assignment of suit and value
         // was successful, errorFlag is false because that means
         // there are no errors. It might be clearer if errorFlag would be
         // called isError instead.
         return true;
      } 
      else
      {   
         // the set method failed, set errorFlag to indicate the error.
         // return false
         errorFlag = true;
         return false;
      } // if-else  
    
   } // set(char, Suit) 
 
 
   /** GETVALUE()
    * 
    * @return
    */
   public char getValue()
   {
      return value;
   } // getValue
 
 
   /** GETSUIT()
    * 
    * @return
    */
   public Suit getSuit()
   {
      return suit;
   } //  getSuit
 
 
   /** GETERRORFLAG()
    * 
    * @return
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   } // getErrorFlag()
 
 
   /** EQUALS(Card)
    * 
    * @param aCard
    * @return
    */
   public boolean equals(Card aCard)
   {
      if (suit == aCard.suit && value == 
            aCard.value && errorFlag == aCard.errorFlag)
      {  
         return true;
      }
      else
      {
         return false;
      }
    
   } // equals(Card)
 
 
   /** ISVALID(char, Suit)
    * 
    * @param aValue
    * @param aSuit
    * @return
    */
   private boolean isValid (char aValue, Suit aSuit)
   {
      // Assume that the input is false
      boolean localFlag = false;
    
      // Check if aValue is valid. If it is, change localFlag
      switch (aValue)
      {
         case 'A':
            localFlag = true;
            break;
         case '2':
            localFlag = true;
            break;
         case '3':
            localFlag = true;
            break;
         case '4':
            localFlag = true;
            break;
         case '5':
            localFlag = true;
            break;
         case '6':
            localFlag = true;
            break;
         case '7':
            localFlag = true;
            break;
         case '8':
            localFlag = true;
            break;
         case '9':
            localFlag = true;
            break;
         case 'T':
            localFlag = true;
            break;
         case 'J':
            localFlag = true;
            break;
         case 'Q':
            localFlag = true;
            break;
         case 'K':
            localFlag = true;
            break;
         case 'X':
            localFlag = true;
            break;
         default:
            localFlag = false;
            break;
    } // switch
    
    // If the value is valid, then we'll check the legality of the suit
    // we don't need to check here if the aValue argument is bad.
    
    // in the for-each loop below, we need to have an array of enums.
    // the values method of the enum will return an array
    //Card.Suit[] SuitArray = Suit.values(); is one way, but we can just
    // use Suit.values()
  
    if (localFlag == true)
    {
       // here we iterate through an array of the suit enum.
       // at each loop, we compare the enums to the argument enum value.
       for (Suit localSuit : Suit.values())
       {
          if (localSuit == aSuit)
          {  
             // the suit argument aSuit matches one of the enums, so all good.
             return localFlag = true;
          }
       } // for
    } // if
    
    // if we made it through the for loop without matching an enum, we
    // end up here. Which means aSuit was invalid.
    // so return false.
    return localFlag = false;
 } // isValid(char, Suit)
 
} // Card class


class Deck
{
 public final static int CARDS_PER_DECK = 52 + 4; // adding the 4 jokers
 public final static int MAX_DECKS = 6;
 public final static int MAX_CARDS_DECK = MAX_DECKS * CARDS_PER_DECK;
 
 // do not add jokers to the masterpack because they will be added
 // by the CardGameOutline class later one.
 private static Card[] masterPack = null;
 private Card[] cards;
 private int topCard; 

 
 /** DECK()
  * @note:   
  */
 public Deck()
 {
    int numPacks = 1;
    allocateMasterPack(); 
    init(numPacks);
 } // Deck()

 
 /** DECK(INT)
  * 
  * @param numPacks
  */
 public Deck(int numPacks)
 {
    allocateMasterPack();
    init(numPacks);
 } // Deck(int)
    
 
 /** INIT(int)
  * 
  * @param numPacks
  */
 public void init(int numPacks)
 {
    // topCard = 0 if there is 1 card left.
    topCard = numPacks * CARDS_PER_DECK - 1; 
    int arraySize = numPacks * CARDS_PER_DECK;
    
    if(numPacks != 0 && arraySize <= MAX_CARDS_DECK)
    {
       cards = null;
       cards = new Card[arraySize];
       
       // re-populate cards[] with CARDS_PER_DECK × numPacks cards
       for (int i = 0; i < numPacks; i++)
       {
          for (int y = CARDS_PER_DECK * i, x = 0;  
                y < CARDS_PER_DECK * i + CARDS_PER_DECK; y++, x++)
          {
             cards[y] = masterPack[x];
          } // for
          
       } // for
       
    }// if
    
    else
    {
       return;
    } // else
    
 } // init
    

 /** SHUFFLE() 
  * 
  */
 public void shuffle()
 {
    Random deckshuffle = new Random();
    for (int i = cards.length - 1; i > 0; i--)
    {
       /*Here I use nextInt(int) to get a random number between (inclusive)
        * and cards.length*/
       int randShuffle = deckshuffle.nextInt(i);
       //Shuffle cards array by using index randShuffle
       Card cardShuffle = cards[randShuffle];
       cards[randShuffle] = cards[i];
       cards[i] = cardShuffle;
    }
          
 } // shuffle
   
 
 /** DEALCARD()
  * 
  * @return
  * @note : adjust this method to account for possible empty spaces in
  * in the array.
  */
 public Card dealCard()
 {         
    // it's ok if topCard = 0, that means there is one card left.
    if (topCard < 0) 
    {
       return null;
    }
    else if (cards[topCard] == null)
    {
       // create an error card
       Card errorCard = new Card('Z', Card.Suit.spades);
       return errorCard;
    }
    else 
    {  
       Card topDeck = new Card(cards[topCard]);
       cards[topCard] = null;
       topCard--;
       
       return topDeck;
    }
 } // dealCard
 
 
 /** ADDCARD(Card)
  * This method will make sure that there are not too many instances 
  * if you add it. This method will put the card on the top of the deck.
  * 
  * @pre : The Card contains nontrivial values.
  * @post : the input card has been placed at the top of the deck.
  * @param card : the card to be added to the top of the deck.
  * @return boolean false is returned if there will be too many of the cards.
  */
 public boolean addCard(Card card)
 {
    boolean isTooMany = false;
    boolean isFound = false;
    int numberOfDuplicates = 0;

    // search for the Card and increment each time it is found.
    // There will be cards.length / CARDS_PER_DECK copies of a SPECIFIC card.
    for (int deckIndex = 0; deckIndex <= topCard; deckIndex++)
    {
       isFound = cards[deckIndex].equals(card);
       if (isFound)
       {
          numberOfDuplicates++;
       }
    }
    
    // make sure the number of duplicates does not 
    // equal cards.length / CARDS_PER_DECK
    // also, we want the topCard index to be at least 2 less than the last index
    isTooMany = numberOfDuplicates >= (cards.length / CARDS_PER_DECK);
    if (! isTooMany && (topCard < cards.length - 1))
    {
       topCard++;
       cards[topCard] = card;
    }
    
    return isTooMany;
 } // addCard(Card)
 
 
 /** REMOVECARD(Card)
  * This method will remove a specific Card from the deck. Then this method
  * will put the current top card into its place. 
  * 
  * @pre : the card specified is actually in the deck. This card will 
  *        have a specific value and suit.
  * @post : the specified card (value and suit) is removed from the deck.
  * @param card : this is the specific Card that will be removed.
  * @return boolean : return false if the target card is not found.              
  */
 public boolean removeCard(Card card)
 {
    boolean isFound = false;
    // search the Deck for target card.
    for (int deckIndex = 0; deckIndex <= topCard; deckIndex++)
    {
       isFound = cards[deckIndex].equals(card);
       if (isFound)
       {
          if (deckIndex == topCard) // the target card was the topcard
     {
    cards[topCard] = null;
    topCard--;
          }
          else
     {  
             // set current card to null then assign topCard Card to this card
             cards[deckIndex] = null;
             cards[deckIndex] = new Card(cards[topCard]);
             cards[topCard] = null;
             topCard--;
          } // if-else
       } // if
    } // for
    
    return isFound;
 } // removeCard(Card)
 
 
 /** GETNUMCARDS()
  * This method will return the number of cards remaining in the deck.
  * 
  */
 public int getNumCards()
 {
    // topCard also keeps track of full the deck is, but we need to add
    // one since the topCard also acts as the array index.
    return getTopCard() + 1; 
 }
    
 
 /** GETTOPCARD()
  * 
  * @return
  */
 public int getTopCard()
 {  
    return topCard;
 } // getTopCard
 
 
 /** INSPECTCARD(int)
  * 
  * @param k
  * @return
  */
 public Card inspectCard(int k)
 {
    if (topCard < 0 || k > topCard || k < 0)
    {
       Card errorCard = new Card('Z', Card.Suit.spades);
       return errorCard;
    }
    else
    {
       Card aCard = new Card(cards[k]);
       return aCard;
    }
 } // inspectCard
 
 
 /** ALLOCATEDMASTERPACK()
  * 
  */
 private static void allocateMasterPack()
 {  
    if (masterPack == null)
    {
       masterPack = new Card[CARDS_PER_DECK]; 
       int cardPos = 0;
               
       for (Card.Suit mySuit : Card.Suit.values())
       {
          for (int i = 1; i < 15; i++)
          {
             masterPack[cardPos] = new Card();
             
             if (i == 1)
             {
                masterPack[cardPos].set('A', mySuit);
             }
             else if (i == 10)
             {
                masterPack[cardPos].set('T', mySuit);
             }
             else if (i == 11)
             {
                masterPack[cardPos].set('J', mySuit);
             }
             else if (i == 12)
             {
                masterPack[cardPos].set('Q', mySuit);
             }
             else if (i == 13)
             {
                masterPack[cardPos].set('K', mySuit);
             }
             else
             {
                masterPack[cardPos].set((char)(i + '0'), mySuit);
             } // if-else
             
             cardPos++; // this will increment until CARDS_PER_DECK - 1
          } // for
       } // for
    } // if       
  } // allocateMasterPack
 
 
 /** SORT()
  * This method will put all of the cards in the deck back into the 
  * right order according to their values.
  * 
  * @note : Is there another method somewhere that already does
  * this that you could refer to?
  * @note : this method will rely on the arraySort method of the Card class
  */
 public void sort()
 {
    Card.arraySort(cards, getNumCards());
 } // sort()
 
} // Deck Class definition  

class Hand
{
 // This array represents the cards in a player's hand that will be used
 // for a round of play. This array changes as cards are played or added. 
 // The size of this array is determined from MAX_CARDS_HAND
 private Card[] myCards;
 private int numOfCannotPlays = 0;
 
 // this int value is used to keep track of the top array element in
 // myCards array.
 private int numCards;
 
 public static final int MAX_CARDS_HAND = 100;
 
 
 /** HAND()
  * myCards will not be initialized with Card objects yet.
  * Loading up the myCards array is done with the takeCard method. 
  */
 public Hand()
 {
    myCards = new Card[MAX_CARDS_HAND];
    numCards = 0;  
 } // Hand()
   
 
 // RESETHAND()
 public void resetHand()
 {
    myCards = null;
    myCards = new Card[MAX_CARDS_HAND];
    numCards = 0;
 } // resetHand
 
 
 /** TAKECARD(Card)
  * This method will add the aCard argument to the myCards array at index
  * numCards and then increment numCards. The method will make a deep copy
  * using the copy constructor of the Card class.
  * 
  * @param aCard : Card that is dealt to the Hand from the Deck
  * @return boolean : a true value indicates that the Card argument
  *                   was valid and non-null
  * @note : This method will rely on the Card copy constructor to 
  *         automatically set the errorFlag.
  */
 public boolean takeCard(Card aCard)
 {
    if (aCard == null)
    {
       return false;
    }
    
    if (numCards >= MAX_CARDS_HAND || numCards < 0)
    {
       return false;
    }
     
    myCards[numCards] = new Card(aCard);
       
    boolean cardErrorState = ! myCards[numCards].getErrorFlag();
    
    numCards++;
    
    // we will return true here IF the errorFlag was set to false
    // and we return true here IF the errorFlag was set to true.
    return cardErrorState; 
 } // takeCard

 
 /** PLAYCARD()
  * This method returns the Card object at the (numCards - 1) index position.
  * The method then decrements numCards. 
  * 
  * @pre : The myCards array has cards and also the numCards value is 
  *        greater than 0 but less than or equal to MAX_CARDS_HAND
  * @post : a Card is returned, and the numCards value is reduced by one. 
  * @return Card : card object at the index numCards - 1
  * @note : This method will rely on the Card copy constructor.
  */
 public Card playCard()
 {
    Card aCard = new Card(myCards[numCards - 1]);
    myCards[numCards - 1] = null;
    
    numCards--;
    
    return aCard;   
 } // playCard
 
 
 /** PLAYCARD(int)
  * This method will remove the Card at a location and slide all of the Cards
  * down one spot in the myCards array.
  * @param cardIndex
  * @return
  */
 public Card playCard(int cardIndex)
 {
    if (numCards == 0) // error
    {
       //crates a card that does not work
       return new Card('M', Card.Suit.spades);
    }
    //decrease numCards
    Card card = myCards[cardIndex];
    
    numCards--;
    for (int i = cardIndex; i < numCards; i++)
    {
       myCards[i] = myCards[i + 1];
    }
    
    myCards[numCards] = null;
    return card;
 } // playCard(int)
 
    
 /** TOSTRING()
  * This method will display the Cards in the hand by iterating through
  * myCards array from index 0 to end.
  * 
  * @pre : there exists at least 1 card in the hand.
  * @post : this method will return a string with the Card values.
  * @return String : the string which shows all the cards in the hand.
  * @note : this method will rely on the toString method of the Card class.
  */
 public String toString()
 {
    String myHand = "Hand = (";
    
    if (numCards == 0)
    {
       return "Hand( )";
    }
    else
    {
       for (int index = 0; index < numCards; index++)
       {
          if (index % 10 == 0)
          {
             myHand += "\n";
          }
          
          if (index == numCards - 1)
          {
             myHand += " [" + index + "] " + myCards[index].toString() + " )";
          }
          else
          {
             myHand += " [" + index + "] " + myCards[index].toString() + ", ";
          } // if-else
       } // for
    } // if-else
 
    return myHand;
 } // Hand - toString


 /** GETNUMCARDS()
  * 
  * @return*/
 public int getNumCards()
 {
    return numCards;
 }// getNumCards
 
 
 /** INSPECTCARD(int)
  * This is an accessor method for an individual card in the myCards array.
  * @param k : this int value represents an index position in the 
  * myCards array.
  * @pre : the vlaue k is within the bounds of the myCards array.
  * @post : the method returns the Card at index k.
  * @return
  * @note : if the k index value is out of bounds, this method will return
  * a Card with the errorFlag intentionally set to true. Thid method will
  * rely on the set() method of the Card class.
  */
 public Card inspectCard(int k)
 {        
    if (k > numCards || k < 0 || numCards == 0)
    {
       // this was a bad input, return a card with error.
       // sending the char Z will cause the Card constructor to set
       // the errorFlag.
       Card errorCard = new Card('Z', Card.Suit.spades);
       return errorCard;
    }
    
    else
    {
       // returning the Card at the k index.
       return myCards[k];
    }
    
 } // inspectCard 
 
 
 /** SORT()
  * This method will put all of the cards in the Hand back into the 
  * right order according to their values.
  * 
  * @note : Is there another method somewhere that already does
  * this that you could refer to?
  * @note : this method will rely on the arraySort method of the Card class
  */
 public void sort()
 {
    Card.arraySort(myCards, getNumCards());
 } // sort()
 
 public void addToCannotPlay()
 {
    numOfCannotPlays++;
 }
 
 public int getNumOfCannotPlays()
 {
    return numOfCannotPlays;
 }

} // Hand Class definition
