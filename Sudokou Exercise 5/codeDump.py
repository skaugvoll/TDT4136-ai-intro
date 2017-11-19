def revised(self,i,assignment,j):
    revised = False
    foundY = False
    di = assignment[i]
    print("di: " + str(di))
    dj = assignment[j]

    for x in di:
        constraints = self.constraints[i][j]
        for y in dj:
            tempTup = (x, y)

            if tempTup in constraints:
                foundY = True
                break
            if not foundY:
                di.remove(x)
                revised = True

    return revised
