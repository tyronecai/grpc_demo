# coding=utf-8
from concurrent import futures
import time

import google
import grpc
from proto import product_pb2
from proto import product_pb2_grpc
from proto import book_pb2
from proto import book_pb2_grpc


class ProductImpl(product_pb2_grpc.ProductInfoServicer):
    # 工作函数
    def addProduct(self, request, context):
        print(request)
        return product_pb2.ProductId(value=str(time.time()))

    def getProduct(self, request, context):
        print(request)
        return product_pb2.Product(id=str(time.time()), name="xxxx", description="XXX")


class BookImpl(book_pb2_grpc.BookServiceServicer):
    # 工作函数
    def addBook(self, request, context):
        print(request)
        return google.protobuf.wrappers_pb2.StringValue(value="明清小说")

    def getBook(self, request, context):
        print('getBook', request)
        return book_pb2.Book(id="ss", name="xxx", price=time.time())

    def searchBooks(self, request, context):
        print('searchBooks', request)
        for x in range(10):
            yield book_pb2.Book(id=str(x), name="xxx", price=time.time())

    def updateBooks(self, request_iterator, context):
        for request in request_iterator:
            print('updateBook', request)
        return google.protobuf.wrappers_pb2.StringValue(value="明清小说")

    def processBooks(self, request_iterator, context):
        for request in request_iterator:
            print('processBook', request)
            yield book_pb2.BookSet(id=str(time.time()))


def serve():
    # gRPC 服务器
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    product_pb2_grpc.add_ProductInfoServicer_to_server(ProductImpl(), server)
    book_pb2_grpc.add_BookServiceServicer_to_server(BookImpl(), server)

    server.add_insecure_port('127.0.0.1:50051')
    print("sever is opening, waiting for message...")
    server.start()  # start() 不会阻塞，如果运行时你的代码没有其它的事情可做，你可能需要循环等待。
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()
