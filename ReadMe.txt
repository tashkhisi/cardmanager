# Card Manager

This project is created to work as a card manager for situation that the total amount of transaction is more than 
the transferable amount of one card (3,000,000 toman) 

The problem seams to be very straightforward at first but it gets complicated when we want to implement this project using java multi threading, constraints must be reset at 00:00 but when we are using thread pool executor it gets complicated.

At first we want to find the minimum number of transaction that fulfil our totalAmount, imagine that we have
three cards with 500,000, 1,000,000, 1,000,000, 2,000,000, 500,000 and our totalAmount is 2,500,000 so in this situation
the cards with 2,000,000 and 1,000,000 must be chosen by the algorithm in order to have minimum number of cards in transaction
in this situation the card with 2,000,000 transferable amount completely transferred and 500,000 will be transferred from the other card.

Here for minimizing number of card we use greedy algorithm, in other word to minimize number of card we prioritize cards
by their existing  transferable amount for example in above example first we chose 2,000,000(which has maximum transferable amount) then we chose 1,000,000 which has maximum transferable amount after previous card 

Calling service for transferring money from bank service is another part of problem because this part is time consuming(calling service from bank service and getting response) for the sake of being scalable we have chosen to do this part on multiple thread and  because this concern is beyond the scope of the service and cardManager class(which we have implemented so far), this part is implemented in other class with name CardManagerNonBlockingTransferHelper(for the sake of being maintainable), we call method of this class and this class distribute task on multiple thread in threadPool Executor but the problem is that we don't know when our request received by the bank service and when the response gets back even we don't know when our thread gets to run on thread pool so imagine its 11:50 pm and our stuff request us to do transfer, but we only create thread and pass it to executor and we don't know when those thread gets to run, for example when they received by bank at 12:05 and  the status is successful we have to reduce transferable amount of my card in following day(because bank receive it after 12:00). sure we have a job that is executed at 12:00 pm but if we want to rely on our data there are many unreliable situation, for example if we reset all of our constraint at 12 pm but one of our transfer transaction request  has been sent to the bank before 11:55 and response receive at 12:05, the transaction is registered for tomorrow but we reset its transferable amount to 3,000,000 before we get that response its something like domino effect and we prefer to prevent that so we decided to reset constraint based on the data that we get from bank service(please notice that this  service will be called once a day or only when application restart) and  put scheduler constraints at 11:30, 11:50, 12:05
At 11:30 with job scheduleTaskForBlockingComingRequest we don't receive bank transfer request from our stuff any more
At 12:05 with job scheduleForResetConstraints we wait until there is no task in the thread pool and then we reset all constraints 
After 12:05 we are pretty sure that there is no more task that are running but there are rare situation that leave us in unreliable data
for example imagine we send request to bank at 11:50 and response is received at 12:05

This project is not completed yet and I'm working on it

 

## Authors

* **ehsan Tashkhisi

