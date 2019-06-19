from numpy import where, logical_and

def execute(*args):
    """ Perform scalar subtraction; adjust if difference is zero but inputs are not.
    """
    diffArgs = list(args)
    result = 0
    result += diffArgs[0]
    for i in range(1, len(diffArgs)):
        result -= diffArgs[i]
    return where(logical_and(sum(diffArgs) != 0.0, result == 0.0), 0.01, result)
