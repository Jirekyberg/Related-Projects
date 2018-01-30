from tkinter import *
from time import *
from math import *
from random import *
import winsound 

root = Tk()
s = Canvas(root, width=700, height=900, background = "cornsilk2")

#the interface of which the game starts with along with basic values
def runGame():
    global interface,JourneyName,speedMulti,noteMulti,endless,score,length,width,q,w,e,r,title

    speedMulti = 1.75
    noteMulti = 1.6
    length = 75
    width = 30
    interface = "yes"

    title = s.create_text (350, 400, text = "Buts-ka", font = "Verdana 30 bold")
    
    JourneyName = Button( root, text = "Don't Stop Believing", font = "Times 18 italic", command = Journey, anchor = CENTER)
    JourneyName.pack()
    JourneyName.place(x=120, y=750,width=250)
    
    endless = Button ( root, text = "Random", font = "Times 18 italic", command = random, anchor = CENTER)
    endless.pack()
    endless.place (x=440, y =750, width=100)
    
    s.update()

#the second interface/the ending interface, it is similar to the first one but shows the score of the last game 
def initialInterface1():
    global interface,JourneyName,speedMulti,noteMulti,endless,score,length,width,q,w,e,r,title

    speedMulti = 1.25
    noteMulti = 2.475
    length = 75
    width = 30

    interface = "yes"

    title = s.create_text (350, 400, text = "Buts-ka", font = "Verdana 30 bold")
    JourneyName = Button( root, text = "Don't Stop Believing", font = "Times 18 italic", command = Journey, anchor = CENTER)
    JourneyName.pack()
    JourneyName.place(x=120, y=750,width=250)

    endless = Button ( root, text = "Random", font = "Times 18 italic", command = random, anchor = CENTER)
    endless.pack()
    endless.place (x=440, y =750, width=100)


    percentage = int(score * 100 / (len(q)+len(w)+len(e)+len(r)))
    lastScore = s.create_text(70,30, text = "last score:  " + str(percentage) + "%", font = "Verdana 10")

    s.update()
    
#keyboard dectection, as the game requries to use keys 'q','w','e','r'
def keyMotion (event):
    global x1,x2,x3,x4,y1,y2,y3,y4,s1,s2,s3,s4,score,circleWidths1,circleLengths1,circleWidths2,circleLengths2,circleWidths3,circleLengths3,circleWidths4,circleLengths4,i
    if event.keysym == "l":
        print (i)
    
    for k in range(s1):
        if event.keysym == "q":
            if 575 < y1[k] + 0.5 * circleWidths1[k] < 750:
                score = score + 1
                x1.remove (x1[k])
                y1.remove (y1[k])
                circleWidths1.remove (circleWidths1[k])
                circleLengths1.remove (circleLengths1[k])
                s1 = s1 - 1
            
    for k in range(s2):
        if 575 < y2[k] + 0.5 * circleWidths2[k] < 750:
            if event.keysym == "w":
                score = score + 1
                x2.remove (x2[k])
                y2.remove (y2[k])
                circleWidths2.remove (circleWidths2[k])
                circleLengths2.remove (circleLengths2[k])
                s2 = s2 - 1

    for k in range(s3):
        if 575 < y3[k] + 0.5 * circleWidths3[k] < 750:
            if event.keysym == "e":
                score = score + 1
                x3.remove (x3[k])
                y3.remove (y3[k])
                circleWidths3.remove (circleWidths3[k])
                circleLengths3.remove (circleLengths3[k])
                s3 = s3 - 1
                
            
    for k in range(s4):
        if 575 < y4[k] + 0.5 * circleWidths4[k] < 750:
            if event.keysym == "r":
                score = score + 1
                x4.remove (x4[k])
                y4.remove (y4[k])
                circleWidths4.remove (circleWidths4[k])
                circleLengths4.remove (circleLengths4[k])
                s4 = s4 - 1

#deletes the starting interface and loads the string track
def createTrack():
    global JourneyName,endless,title

    # do not delete buttons for option of retrying early on
    JourneyName.destroy()
    endless.destroy()
    s.delete(title)
    
    s.create_rectangle(0,0,700,920,fill = "cornsilk2")
    
    x1 = 50
    x2 = 200
    for i in range (5):
        s.create_line(x1,725, x2, 100, fill = "gray30", width = .5)
        x1 = x1 + 150
        x2 = x2 + 75


    s.create_text(75,710, text = "Q" , font = "20")
    s.create_text(220,710, text = "W" , font = "20")
    s.create_text(370,710, text = "E" , font = "20")
    s.create_text(520,710, text = "R" , font = "20")
        
    s.create_line(80,600, 620,600, fill = "gray30", width = .5)
    s.create_line(50,725, 650,725, fill = "gray30", width = .5)
    
    for i in range (3):
        countDown = s.create_text(350,500,text = 3 - i, font = "Times 20 italic")

        s.update()
        sleep(1)
        s.delete (countDown)

#Creates the updated notes based onto their new position and updated corner values
def noteCreation(lineNumber,k):
    global circleWidths1,circleLengths1,circleWidths2,circleLengths2,circleWidths3,circleLengths3,circleWidths4,circleLengths4,sphere1,sphere2,sphere3,sphere4,x1,x2,x3,x4,y1,y2,y3,y4,s1,s2,s3,s4

    if lineNumber == 1:
        sphere1[k] = s.create_oval(x1[k],y1[k], x1[k]+circleLengths1[k], y1[k]+circleWidths1[k], fill = "black")
        
    elif lineNumber == 2:
        sphere2[k] = s.create_oval(x2[k]+2,y2[k], 348, y2[k]+circleWidths2[k]-1, fill = "black")

    elif lineNumber == 3:
        sphere3[k] = s.create_oval(352,y3[k], x3[k]+circleLengths3[k]+2, y3[k]+circleWidths3[k], fill = "black")

    else:
        sphere4[k] = s.create_oval(x4[k],y4[k], x4[k]+circleLengths4[k]-3, y4[k]+circleWidths4[k], fill = "black")

        
#Updates the seperate notes for the next frame and descending/expanding rate
def noteUpdate(lineNumber,k):
    global speedMulti, noteMulti,x1,x2,x3,x4,y1,y2,y3,y4,s1,s2,s3,s4,circleWidths1,circleLengths1,circleWidths2,circleLengths2,circleWidths3,circleLengths3,circleWidths4,circleLengths4,sphere1,sphere2,sphere3,sphere4


    if lineNumber == 1:
        x1[k] = x1[k] - 1.65 * speedMulti
        y1[k] = y1[k] + 6.75 * speedMulti
        circleLengths1[k] = circleLengths1[k] + 0.75 * speedMulti
        circleWidths1[k] = circleWidths1[k] + 0.8 * speedMulti

    elif lineNumber == 2:
        x2[k] = x2[k] - 0.80 * speedMulti
        y2[k] = y2[k] + 6.75 * speedMulti
        circleWidths2[k] = circleWidths2[k] + 0.8 * speedMulti

    elif lineNumber == 3:
        y3[k] = y3[k] + 6.75 * speedMulti
        circleLengths3[k] = circleLengths3[k] + 0.8 * speedMulti
        circleWidths3[k] = circleWidths3[k] + 0.8 * speedMulti

    else:
        x4[k] = x4[k] + 0.95 * speedMulti
        y4[k] = y4[k] + 6.75 * speedMulti
        circleLengths4[k] = circleLengths4[k] + 0.75 * speedMulti
        circleWidths4[k] = circleWidths4[k] + 0.8 * speedMulti

#The setup for the notes specifically for the Don't stop believing sound track
def journeyNotesSetup():
    global speedMulti, noteMulti,x1,x2,x3,x4,y1,y2,y3,y4,s1,s2,s3,s4,circleWidths1,circleLengths1,circleWidths2,circleLengths2,circleWidths3,circleLengths3,circleWidths4,circleLengths4,sphere1,sphere2,sphere3,sphere4

    s1 = 0
    s2 = 0
    s3 = 0
    s4 = 0
    
    sphere1 = []
    sphere2 = []
    sphere3 = []
    sphere4 = []
    lines1 = []
    lines2 = []
    lines3 = []
    lines4 = []
    x1 = []
    x2 = []
    x3 = []
    x4 = []
    y1 = []
    y2 = []
    y3 = []
    y4 = []
    circleWidths1 = []
    circleLengths1 = []
    circleWidths2 = []
    circleLengths2 = []
    circleWidths3 = []
    circleLengths3 = []
    circleWidths4 = []
    circleLengths4 = []

    for i in range (150):
        circleLengths1.append(length)
        circleWidths1.append(width)
        circleLengths2.append(length)
        circleWidths2.append(width)
        circleLengths3.append(length)
        circleWidths3.append(width)
        circleLengths4.append(length)
        circleWidths4.append(width)
        x1.append (197)
        x2.append (274)
        x3.append (347)
        x4.append (428)
        y1.append(100)
        y2.append(100)
        y3.append(100)
        y4.append(100)
        sphere1.append (0)
        sphere2.append (0)
        sphere3.append (0)
        sphere4.append (0)

#Main procedure for actually creating the notes and storing all the corresponding spawn frames for the notes
def createJourneyNotes():
    global sphere,x,y,speedMulti, noteMulti,score,lines1,lines2,lines3,lines4,s1,s2,s3,s4,q,w,e,r,i

    #prepare the base values for the notes of the song
    journeyNotesSetup()      

    #list of notes and their corresponding spawn frames/spawn strings relative to frames/beat
    q = [20,40,60,80,                180,200,220,240,                340,360,380,400,                500,520,540,560,                     856,923,941,         1050,1090,1163,1210,            1380,1542,1562,1588,      1727,1747,1802,      1965, 2040,2060,2080,2100,2120,2140,2160,2180,2200,2220,2240,2260,2280,2300, 2351,2378,2407,2435,2463,2491, 2600,2610,  2669,2715,           2853,            2978,3018,3083,                                               3283,3310,                                      3510,3525,                            3620,     3640,                   3690,          3720,3730,          3760,3770,3780,               3820,3825,              3890,          3925,          3970,                     4040,                                                         4185,                         4285,4295,          4320,     4340,    4415,     4445,4475,            4555,4565,4575,                    4625,4635,                              4710,              4750,4770,4780,4790,5800]
    w = [             90,110,130,150,                250,270,290,310,                410,430,450,470,                570,590,610,630,     731,766,866,911,923, 1040,1070,1100,1135,1200,       1390,1410,1522,1552,1600, 1717,1762,1802,1835, 1955, 2050,2090,2130,2170,2210,2250,2290,                                    2344,2372,2400,2428,2456,2484,             2692,2709,           2838,2873,       2968,3008,3028,3073,3100,3120,3145,3170,3195,3220,3235,3240,  3293,3357,3393,           3445,             3494,         3540,     3575,         3610,    3630,3640,     3660,              3700,3710,     3730,     3750,          3780,                    3830,         3860,     3905,          3950,                     4020,               4060,      4100,4110,4120,4130,               4160,      4205,               4265,               4305,     4330,4340,         4420,4445,4475,                          4545,     4555,4565,4575,4585,                         4645,4655,4665,4675,4690,4700,     4720,         4750, 4760,4780,4790,5800]   
    e = [10,30,50,70,100,120,140,160,170,190,210,230,260,280,300,320,330,350,370,390,420,440,460,480,490,510,530,550,580,600,620,640,650, 721,743,877,911,941, 1030,1080,1110,1135,1163,1200,  1400,1428,1532,1577,1600, 1697,1737,1835,1860, 1945, 2070,2110,2150,2190,2230,2270,2310,                                    2337,2365,2393,2421,2449,2477,             2685,2701,2727,2739, 2823,2888,2903,  2988,3053,2993,3105,3125,3150,3175,3200,3225,3235,3240,       3310,3367,           3430,              3487,                  3575,              3610,              3650,     3670,              3710,3720,     3740,3750,               3790,     3810,              3850,               3915,          3960,           4010,     4030,               4070,           4120,4130,          4150,                4225,     4275,          4295,               4330,4340,         4420,          4515,4530,                     4545,                    4595,     4615,4625,4635,                                        4730,    4750,4790,5800]                      
    r = [                                                                                                                                 710,766,888,         1060,1120,1200,                 1428,1512,1588,           1707,1777,1860,      1935,                                                                        2330,2358,2386,2414,2442,2470, 2600,2610,  2677,2727,           2813,2858,2903,  2998,3063,                                                    3393,            3415,              3480,                               3585,3595,3610,                             3680,                        3740,                         3800,               3840,         3890,          3925,          3970, 4000,                    4050,                               4140,4140,                          4255,                    4305,4305,     4330,         4415,               4515,4530,                                                   4605,                              4675,4590,4700,              4740,4750, 4760,5800]


    for i in range (12000):
    #play the sound track right before it actually starts to prevent lag in loading the sound on the correct frame
        if i == 11:
            winsound.PlaySound("Don't stop.wav", winsound.SND_FILENAME| winsound.SND_ASYNC)
        if i == 13:
            winsound.PlaySound("Don't stop.wav", winsound.SND_FILENAME| winsound.SND_ASYNC)

        #check everyframe for if there is a value in the array that matches the current frame. If there is, the amount of times the loop which creates and updates the new notes is increased by 1, therfor generating a new note from the top
        #noteMulti is something that converts frames/beat
        for k in range (len(q)):
            if i == int(q[k] * noteMulti):
                s1 = s1 + 1
        for k in range (len(w)):
            if i == int(w[k] * noteMulti):
                s2 = s2 + 1
        for k in range (len(e)):
            if i == int(e[k] * noteMulti):
                s3 = s3 + 1
        for k in range (len(r)):
            if i == int(r[k] * noteMulti):
                s4 = s4 + 1

        #Update the note positions
        for k in range (s1):
            noteCreation(1,k)
            noteUpdate(1,k)
        for k in range (s2):
            noteCreation(2,k)
            noteUpdate(2,k)
        for k in range (s3):
            noteCreation(3,k)
            noteUpdate(3,k)
        for k in range (s4):
            noteCreation(4,k)
            noteUpdate(4,k)
            
        board = s.create_text(600,100, text = "score:" + str(score), font = "Verdana 15")
        
        s.update()
        sleep (0.01)
        s.delete (board)
        for k in range (len(sphere1)):
            s.delete(sphere1[k])
            s.delete(sphere2[k])
            s.delete(sphere3[k])
            s.delete(sphere4[k])

        
##Game mode 2 setup routines
#Get all the randomized values for the array of which fram each note will spawn in on q,w,e and r and set basic requirement values
def  randomNoteSetup():
    global q,w,e,r,speed,x1,x2,x3,x4,y1,y2,y3,y4,s1,s2,s3,s4,circleWidths1,circleLengths1,circleWidths2,circleLengths2,circleWidths3,circleLengths3,circleWidths4,circleLengths4,sphere1,sphere2,sphere3,sphere4,speedMulti, noteMulti

    q = []
    w = []
    e = []
    r = []

    speed = 9
    #a minimum of 150 notes will be spawned and stored
    for i in range (150):
        lineNumber2 = 0
        lineNumber = randint(1,4)
        
        #there is a 1 out of 6 chance that there will be 2 notes spawning on the same beat
        double = randint (1,6)

        if double == 1:
            lineNumber2 = randint(1,4)

        if lineNumber == 1:
            q.append(i*speed)
            
        elif lineNumber == 2:
            w.append(i*speed)
            
        elif lineNumber == 3:
            e.append(i*speed)
            
        elif lineNumber == 4:
            r.append(i*speed)
            
        if lineNumber2 == 1:
            q.append(i*speed)
            
        elif lineNumber2 == 2:
            w.append(i*speed)
            
        elif lineNumber2 == 3:
            e.append(i*speed)
            
        elif lineNumber2 == 4:
            r.append(i*speed)

    s1 = 0
    s2 = 0
    s3 = 0
    s4 = 0
    
    sphere1 = []
    sphere2 = []
    sphere3 = []
    sphere4 = []
    lines1 = []
    lines2 = []
    lines3 = []
    lines4 = []
    x1 = []
    x2 = []
    x3 = []
    x4 = []
    y1 = []
    y2 = []
    y3 = []
    y4 = []
    circleWidths1 = []
    circleLengths1 = []
    circleWidths2 = []
    circleLengths2 = []
    circleWidths3 = []
    circleLengths3 = []
    circleWidths4 = []
    circleLengths4 = []

    for i in range (150):
        circleLengths1.append(length)
        circleWidths1.append(width)
        circleLengths2.append(length)
        circleWidths2.append(width)
        circleLengths3.append(length)
        circleWidths3.append(width)
        circleLengths4.append(length)
        circleWidths4.append(width)
        x1.append (197)
        x2.append (274)
        x3.append (347)
        x4.append (428)
        y1.append(100)
        y2.append(100)
        y3.append(100)
        y4.append(100)
        sphere1.append (0)
        sphere2.append (0)
        sphere3.append (0)
        sphere4.append (0)

#Generating the notes running down the strings
def createRandomNotes():
    global q,w,e,r,speed,score,sphere,x,y,speedMulti, noteMulti,lines1,lines2,lines3,lines4,s1,s2,s3,s4

    #prepare the base values for the notes of the song
    randomNoteSetup()
    
    s1 = 0
    s2 = 0
    s3 = 0
    s4 = 0
    for i in range (3475): 
        #check everyframe for if there is a value in the array that matches the current frame. If there is, the amount of times the next loop which creates and updates the new notes is increased by 1, therfor generating a new note
        for k in range (len(q)):
            if i == int(q[k] * noteMulti):
                s1 = s1 + 1
        for k in range (len(w)):
            if i == int(w[k] * noteMulti):
                s2 = s2 + 1
        for k in range (len(e)):
            if i == int(e[k] * noteMulti):
                s3 = s3 + 1
        for k in range (len(r)):
            if i == int(r[k] * noteMulti):
                s4 = s4 + 1

        #Update the notes 
        for k in range (s1):
            noteCreation(1,k)
            noteUpdate(1,k)
        for k in range (s2):
            noteCreation(2,k)
            noteUpdate(2,k)
        for k in range (s3):
            noteCreation(3,k)
            noteUpdate(3,k)
        for k in range (s4):
            noteCreation(4,k)
            noteUpdate(4,k)
        
        board = s.create_text(600,100, text = "score:" + str(score), font = "Verdana 15")
        
        s.update()
        sleep (0.01)
        s.delete (board)
        for k in range (len(sphere1)):
            s.delete(sphere1[k])
            s.delete(sphere2[k])
            s.delete(sphere3[k])
            s.delete(sphere4[k])
    
##General function that calls all the smaller functions
def Journey():
    global score
    score = 0
    createTrack()
    createJourneyNotes()
    s.create_rectangle(0,0,700,920,fill = "cornsilk2")
    initialInterface1()

def random():
    global score
    score = 0
    createTrack()
    createRandomNotes()
    s.create_rectangle(0,0,700,920,fill = "cornsilk2")
    initialInterface1()
    
root.after(0, runGame)

s.bind( "<Key>", keyMotion)

s.pack()
s.focus_set()
root.mainloop()
