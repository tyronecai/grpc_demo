# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: proto/product.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x13proto/product.proto\x12\x07product\"Z\n\x07Product\x12\n\n\x02id\x18\x01 \x01(\t\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x13\n\x0b\x64\x65scription\x18\x03 \x01(\t\x12\r\n\x05price\x18\x04 \x01(\x02\x12\x11\n\ttimestamp\x18\x05 \x01(\x03\"\x1a\n\tProductId\x12\r\n\x05value\x18\x01 \x01(\t2u\n\x0bProductInfo\x12\x32\n\naddProduct\x12\x10.product.Product\x1a\x12.product.ProductId\x12\x32\n\ngetProduct\x12\x12.product.ProductId\x1a\x10.product.ProductB\'\n\x15org.javaboy.grpc.demoB\x0cProductProtoP\x01\x62\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'proto.product_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'\n\025org.javaboy.grpc.demoB\014ProductProtoP\001'
  _PRODUCT._serialized_start=32
  _PRODUCT._serialized_end=122
  _PRODUCTID._serialized_start=124
  _PRODUCTID._serialized_end=150
  _PRODUCTINFO._serialized_start=152
  _PRODUCTINFO._serialized_end=269
# @@protoc_insertion_point(module_scope)
