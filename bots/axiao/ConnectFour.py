#!/usr/bin/env python2
from copy import copy, deepcopy
import fileinput

max_depth = 3

def solve(node, depth, player, my_piece, other_piece):
    if depth == 0:
        return (node.move, get_value(node.board, my_piece, other_piece))
    if player == 1:
        best_child = (None, -100000000000)
        for child in node.children:
            child_move = solve(child, depth - 1, -player, my_piece, other_piece)
            if best_child[1] < child_move[1]:
                best_child = child_move 
        if depth == max_depth:
            return best_child
        return (node.move, best_child[1])
    else:
        best_child = (None, 1000000000000)
        for child in node.children:
            child_move = solve(child, depth - 1, -player, my_piece, other_piece)
            if best_child[1] > child_move[1]:
                best_child = child_move 
        if depth == max_depth:
            return best_child
        return (node.move, best_child[1])

    
def print_node(node):
    print "move " + str(node.move)
    print "depth " + str(node.depth)
    print "points: " + str(get_value(node.board, 2, 1))
    print ""
    for row in node.board:
        print row
    for child in node.children:
        print ""
        print ""
        print_node(child)

# moves are representing by an int of the column to drop the piece down
class Node:
    # player is either 1 or -1, 1 is you, -1 is the enemy
    def __init__(self, depth, player, board, move):
        self.depth = depth
        self.player = player
        self.board = board
        self.move = move

        if self.player == 1:
            self.player_piece = my_piece
            self.other_piece = other_piece
        else:
            self.player_piece = other_piece
            self.other_piece = my_piece

        self.children = []
        self.create_children()

    def create_children(self):
        if self.depth == 0:
            return
        moves = self.get_next_moves()
        for move in moves:
            next_board = self.apply_move(move)
            self.children.append(Node(self.depth - 1, -self.player, next_board, move))

    def apply_move(self, move):
        next_board = deepcopy(self.board)
        col = self.get_col(move)
        first_piece = 0
        while first_piece < len(col) and col[first_piece]  == ".":
            first_piece += 1
        next_board[first_piece - 1][move] = self.player_piece 
        return next_board 

    def get_col(self, index):
        return [row[index] for row in self.board] 
                
    
    # only consider moves on columns not already full or columns not more than 3 spaces away
    def get_next_moves(self):
        n = len(self.board)
        moves = []

        for i in range(n):
            col = self.get_col(i)
            col = [piece for piece in col if piece != "."]
            if len(col) < n:
                moves.append(i)
        return moves

def get_value(board, player, enemy):
    points = 0
    for row in range(len(board)):
        for col in range(len(board)):
           points += get_points(row, col, board, player, enemy) 
    return points 

def check_constraints(x, y, board):
    if x >= 0 and x < len(board) and y >= 0 and y < len(board):
        return True

def get_points(row, col, board, player, enemy):
    total = 0
    for dx in range(-1, 2):
        for dy in range(-1, 2):
            if dx == 0 and dy == 0:
                continue
            x = col + dx
            y = row + dy
            if check_constraints(x, y, board):
                piece = board[y][x]
                if piece != ".":
                    points = 1
                    chain = 1
                    while check_constraints(x + dx, y + dy, board):
                        x = x + dx
                        y = y + dy
                        next_piece = board[y][x]
                        if next_piece == piece:
                            points = 2 * points + 1
                        else:
                            break
                        chain += 1
                        if chain == 4:
                            points = 10000000000
                            break
                    if piece == player:
                        total += points
                    else:
                        total += -points 
    return total


my_piece = raw_input()
other_piece = "1"
if my_piece == "1":
    other_piece = "2"

n = int(raw_input())
board = []

for i in range(n):
    row = raw_input()
    board.append([])
    for j in range(len(row)):
        board[i].append(row[j])

#board = [[".", ".", ".", ".", "."],[".", ".", "2", ".", "."],[".", ".", "1", ".", "."],[".", ".", "1", ".", "."],["2", "1", "1", "2", "."]]     
node = Node(max_depth, 1, board, 1)
print solve(node, max_depth, 1, my_piece, other_piece)[0] + 1
