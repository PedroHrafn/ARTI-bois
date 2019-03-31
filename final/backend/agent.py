import time

class Agent(object):
    VALUE = 0
    MOVE = 1

    def __init__(self):
        #TODO: initialize agent
        pass
    
    def minimize(self):
        #TODO: implement
        pass

    def maximize(self):
        #TODO: implement
        pass
    
    def evaluateState(self):
        #TODO: implement
        pass
    
    def nextAction(self, state):
        start = time.time()
        h = 1
        try:
            while ret[VALUE]!= 100:
                ret = self.abSearchRoot(h)
                h += 1
        except:
            print(f"Stopped at height: {h}")
            
        returnret[MOVE] 
    
    def abSearchRoot(self, h):