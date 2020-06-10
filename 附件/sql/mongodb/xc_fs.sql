/*
 Navicat Premium Data Transfer

 Source Server         : mongodb
 Source Server Type    : MongoDB
 Source Server Version : 40201
 Source Host           : localhost:27017
 Source Schema         : xc_fs

 Target Server Type    : MongoDB
 Target Server Version : 40201
 File Encoding         : 65001

 Date: 09/06/2020 10:46:48
*/


// ----------------------------
// Collection structure for filesystem
// ----------------------------
db.getCollection("filesystem").drop();
db.createCollection("filesystem");

// ----------------------------
// Documents of filesystem
// ----------------------------
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7COH6AeMAsAAUpclWUmSU998.png",
    filePath: "group1/M00/00/00/rBDXUl7COH6AeMAsAAUpclWUmSU998.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    businesskey: "businesskey",
    filetag: "filetag",
    metadata: {
        name: "metadata"
    },
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7COjOAQCfXAAUpclWUmSU012.png",
    filePath: "group1/M00/00/00/rBDXUl7COjOAQCfXAAUpclWUmSU012.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    businesskey: "businesskey2",
    filetag: "filetag2",
    metadata: {
        name: "metadata2"
    },
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7CQIOAPU7dAAUpclWUmSU104.png",
    filePath: "group1/M00/00/00/rBDXUl7CQIOAPU7dAAUpclWUmSU104.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7M-xiAahWhAAUpclWUmSU551.png",
    filePath: "group1/M00/00/00/rBDXUl7M-xiAahWhAAUpclWUmSU551.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    businesskey: "1",
    filetag: "1",
    metadata: {
        name: "test"
    },
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7M_HWAHFhtAAUpclWUmSU595.png",
    filePath: "group1/M00/00/00/rBDXUl7M_HWAHFhtAAUpclWUmSU595.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7M_neAXX18AAUpclWUmSU140.png",
    filePath: "group1/M00/00/00/rBDXUl7M_neAXX18AAUpclWUmSU140.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7M_uKADY0XAAUpclWUmSU731.png",
    filePath: "group1/M00/00/00/rBDXUl7M_uKADY0XAAUpclWUmSU731.png",
    fileSize: NumberLong("338290"),
    fileName: "y.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7NACGAf6XLAAFuS6th8yM986.jpg",
    filePath: "group1/M00/00/00/rBDXUl7NACGAf6XLAAFuS6th8yM986.jpg",
    fileSize: NumberLong("93771"),
    fileName: "2.jpg",
    fileType: "image/jpeg",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7NAOiAUQcvAChCoFw2QkQ177.png",
    filePath: "group1/M00/00/00/rBDXUl7NAOiAUQcvAChCoFw2QkQ177.png",
    fileSize: NumberLong("2638496"),
    fileName: "1.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7NAfyAeW_6AAFuS6th8yM675.jpg",
    filePath: "group1/M00/00/00/rBDXUl7NAfyAeW_6AAFuS6th8yM675.jpg",
    fileSize: NumberLong("93771"),
    fileName: "2.jpg",
    fileType: "image/jpeg",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7NAt-AVx_tABEe6utt_Lk263.jpg",
    filePath: "group1/M00/00/00/rBDXUl7NAt-AVx_tABEe6utt_Lk263.jpg",
    fileSize: NumberLong("1122026"),
    fileName: "3.jpg",
    fileType: "image/jpeg",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
db.getCollection("filesystem").insert([ {
    _id: "group1/M00/00/00/rBDXUl7N31WANO_IAAA-dLgIoik897.png",
    filePath: "group1/M00/00/00/rBDXUl7N31WANO_IAAA-dLgIoik897.png",
    fileSize: NumberLong("15988"),
    fileName: "7.png",
    fileType: "image/png",
    fileWidth: NumberInt("0"),
    fileHeight: NumberInt("0"),
    filetag: "course",
    _class: "com.xuecheng.framework.domain.filesystem.FileSystem"
} ]);
