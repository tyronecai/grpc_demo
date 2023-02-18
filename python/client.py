# coding=utf-8
from __future__ import print_function

import time

import google
import grpc

from proto import product_pb2
from proto import product_pb2_grpc

from proto import book_pb2
from proto import book_pb2_grpc


def run_product():
    channel = grpc.insecure_channel('127.0.0.1:50051')
    stub = product_pb2_grpc.ProductInfoStub(channel)

    response = stub.addProduct(
        product_pb2.Product(id=str(time.time()), name='Hello World！ This is message from client!'))
    print("addProduct received: %s" % response.value)

    response = stub.getProduct(
        product_pb2.ProductId(value=str(time.time())))
    print("getProduct received: %s" % response.name)


def run_book():
    channel = grpc.insecure_channel('127.0.0.1:50051')
    stub = book_pb2_grpc.BookServiceStub(channel)

    # addBook
    response = stub.addBook(
        book_pb2.Book(id=str(time.time()), name='Hello World！ This is message from client!'))
    print("addBook received: %s, %s" % (type(response), response))

    # getBook
    response = stub.getBook(google.protobuf.wrappers_pb2.StringValue(value="2"))
    print("getBook received: %s, %s" % (type(response), response))

    # searchBooks
    response = stub.searchBooks(google.protobuf.wrappers_pb2.StringValue(value="明清小说"))
    for x in response:
        print("searchBooks received: %s, %s" % (type(x), x))

    # updateBooks
    def generate_book_messages():
        for x in range(10):
            yield book_pb2.Book(id=str(time.time()), name='Hello World！ This is message from client!')

    response = stub.updateBooks(generate_book_messages())
    print("updateBooks received: %s, %s" % (type(response), response))

    # processBooks
    def generate_string_messages():
        for x in range(10):
            yield google.protobuf.wrappers_pb2.StringValue(value=str(time.time()) + ' Hello World！ This is message from client!')
    response = stub.processBooks(generate_string_messages())
    for x in response:
        print("processBooks received: %s, %s" % (type(x), x))


if __name__ == '__main__':
    run_book()
