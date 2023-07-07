High Level Requirements:
-Learning module
-Admin can upload xls based Evaluations (MCQ, Image/Spellings, List of words for jumble, crossword etc)
-Users (having role of STUDENT can attend the evaluations)
-Last evaluation result to be stored in local db
-History of all evaluation attempt to be stored in separate microservice
-

Requirements for another microservice:
-APIs to store data (history data: logins, likes & comments)
-APIs to retrieve data (use 10 mins caching for performance)
-Use MongoDB for performance
-

TODO:
-When other microservice is ready, post the EvalStats results (all evaluation attempts) in mongoDB
-Convert this application example into a plugable webjar as per webjar standard.
-Test this shiksha webjar with NeerApp


-Remove console logs. Use proper debug/info/error logs
-