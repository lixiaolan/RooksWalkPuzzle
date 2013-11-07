#Grass
x1 = 0/2048
x2 = 128./2048
y1 = 0
y2 = 128
for i in range(0,6):
    print "grass"+str(i)
    print "%f,%f,%f,%f"%(x1,x2,y1/2048.,y2/2048.)
    y1+=128
    y2+=128



#Dots
print "horz_dots"
print "%f,%f,%f,%f"%(x1,x2,y1/2048.,y2/2048.)
print "vert_dots"
print "%f,%f,%f,%f"%(x1,x2,(y1+128)/2048.,(y2+128)/2048.)

#Clear
print "clear"
print "%f,%f,%f,%f"%(x1,x2,(y1+256)/2048.,(y2+256)/2048.)

#Menu_Circle
print "menu_circle"
print "%f,%f,%f,%f"%(x1,x2,(y1+3*128)/2048.,(y2+4*128)/2048.)


#Arrows
arrows = ["down_arrow","up_arrow", "left_arrow","right_arrow"]
x1 = 128
x2 = 128+256
y1 = 0
y2 = 256
for i in range(0,4):
    print arrows[i]
    print "%f,%f,%f,%f"%(x1/2048.,x2/2048.,y1/2048.,y2/2048.)
    y1+=256
    y2+=256


#Numbers
x1 = 128+256
x2 = 128+256+256
y1 = 0
y2 = 256
for i in range(0,6):
    print str(i+1)
    print "%f,%f,%f,%f"%(x1/2048.,x2/2048.,y1/2048.,y2/2048.)
    y1+=256
    y2+=256


#Flowers
x1 = (2*256.+128)/2048
x2 = (3*256.+128)/2048
y1 = 0
y2 = 256
for i in  range(0,9):
    print "flower"+str(i)
    print "%f,%f,%f,%f"%(x1,x2,y1/2048.,y2/2048.)
    y1+=256
    y2+=256
 
#Bee
x1 = 128+3*256
x2 = 128+4*256
y1 = 0
y2 = 256
print "bee"
print "%f,%f,%f,%f"%(x1/2048.,x2/2048.,y1/2048.,y2/2048.)
