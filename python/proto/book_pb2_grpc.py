# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc

from google.protobuf import wrappers_pb2 as google_dot_protobuf_dot_wrappers__pb2
from proto import book_pb2 as proto_dot_book__pb2


class BookServiceStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.addBook = channel.unary_unary(
                '/book.BookService/addBook',
                request_serializer=proto_dot_book__pb2.Book.SerializeToString,
                response_deserializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
                )
        self.getBook = channel.unary_unary(
                '/book.BookService/getBook',
                request_serializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
                response_deserializer=proto_dot_book__pb2.Book.FromString,
                )
        self.searchBooks = channel.unary_stream(
                '/book.BookService/searchBooks',
                request_serializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
                response_deserializer=proto_dot_book__pb2.Book.FromString,
                )
        self.updateBooks = channel.stream_unary(
                '/book.BookService/updateBooks',
                request_serializer=proto_dot_book__pb2.Book.SerializeToString,
                response_deserializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
                )
        self.processBooks = channel.stream_stream(
                '/book.BookService/processBooks',
                request_serializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
                response_deserializer=proto_dot_book__pb2.BookSet.FromString,
                )


class BookServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def addBook(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def getBook(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def searchBooks(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def updateBooks(self, request_iterator, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def processBooks(self, request_iterator, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_BookServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'addBook': grpc.unary_unary_rpc_method_handler(
                    servicer.addBook,
                    request_deserializer=proto_dot_book__pb2.Book.FromString,
                    response_serializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
            ),
            'getBook': grpc.unary_unary_rpc_method_handler(
                    servicer.getBook,
                    request_deserializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
                    response_serializer=proto_dot_book__pb2.Book.SerializeToString,
            ),
            'searchBooks': grpc.unary_stream_rpc_method_handler(
                    servicer.searchBooks,
                    request_deserializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
                    response_serializer=proto_dot_book__pb2.Book.SerializeToString,
            ),
            'updateBooks': grpc.stream_unary_rpc_method_handler(
                    servicer.updateBooks,
                    request_deserializer=proto_dot_book__pb2.Book.FromString,
                    response_serializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
            ),
            'processBooks': grpc.stream_stream_rpc_method_handler(
                    servicer.processBooks,
                    request_deserializer=google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
                    response_serializer=proto_dot_book__pb2.BookSet.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'book.BookService', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))


 # This class is part of an EXPERIMENTAL API.
class BookService(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def addBook(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/book.BookService/addBook',
            proto_dot_book__pb2.Book.SerializeToString,
            google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def getBook(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(request, target, '/book.BookService/getBook',
            google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
            proto_dot_book__pb2.Book.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def searchBooks(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_stream(request, target, '/book.BookService/searchBooks',
            google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
            proto_dot_book__pb2.Book.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def updateBooks(request_iterator,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.stream_unary(request_iterator, target, '/book.BookService/updateBooks',
            proto_dot_book__pb2.Book.SerializeToString,
            google_dot_protobuf_dot_wrappers__pb2.StringValue.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)

    @staticmethod
    def processBooks(request_iterator,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.stream_stream(request_iterator, target, '/book.BookService/processBooks',
            google_dot_protobuf_dot_wrappers__pb2.StringValue.SerializeToString,
            proto_dot_book__pb2.BookSet.FromString,
            options, channel_credentials,
            insecure, call_credentials, compression, wait_for_ready, timeout, metadata)
