/* =====================================================================
   Bổ sung CSDL cho: Giỏ hàng - Thanh toán COD
   Chạy trong SSMS (có thể chạy lại nhiều lần, không mất dữ liệu).
   ===================================================================== */
USE LTWeb_De04;
GO

/* 1. Giá bán + tồn kho cho sản phẩm (Videos) */
IF COL_LENGTH('Videos', 'Price') IS NULL
    ALTER TABLE Videos ADD Price DECIMAL(18,0) NOT NULL CONSTRAINT DF_Videos_Price DEFAULT 100000;
IF COL_LENGTH('Videos', 'Stock') IS NULL
    ALTER TABLE Videos ADD Stock INT NOT NULL CONSTRAINT DF_Videos_Stock DEFAULT 50;
GO

/* Giá mẫu 50.000 - 240.000đ (chỉ áp dụng cho sản phẩm còn giá mặc định) */
UPDATE Videos SET Price = 50000 + (ABS(CHECKSUM(VideoId)) % 20) * 10000 WHERE Price = 100000;
GO

/* 2. Danh mục trạng thái đơn hàng */
IF OBJECT_ID('OrderStatus', 'U') IS NULL
CREATE TABLE OrderStatus (
    StatusId   INT           NOT NULL PRIMARY KEY,
    StatusName NVARCHAR(50)  NOT NULL
);
GO
IF NOT EXISTS (SELECT 1 FROM OrderStatus)
INSERT INTO OrderStatus (StatusId, StatusName) VALUES
    (1, N'Đơn hàng mới'),
    (2, N'Đã xác nhận'),
    (3, N'Chuẩn bị hàng'),
    (4, N'Vận chuyển'),
    (5, N'Giao hàng'),
    (6, N'Đã giao'),
    (7, N'Đơn hàng hủy'),
    (8, N'Đơn hàng hoàn');
GO

/* 3. Giỏ hàng (mỗi user - mỗi sản phẩm 1 dòng) */
IF OBJECT_ID('CartItems', 'U') IS NULL
CREATE TABLE CartItems (
    Username NVARCHAR(50) NOT NULL,
    VideoId  NVARCHAR(50) NOT NULL,
    Quantity INT          NOT NULL CONSTRAINT CK_CartItems_Qty CHECK (Quantity > 0),
    AddedAt  DATETIME     NOT NULL CONSTRAINT DF_CartItems_AddedAt DEFAULT GETDATE(),
    CONSTRAINT PK_CartItems PRIMARY KEY (Username, VideoId)
);
GO

/* 4. Đơn hàng + chi tiết đơn hàng */
IF OBJECT_ID('Orders', 'U') IS NULL
CREATE TABLE Orders (
    OrderId       INT IDENTITY(1,1) PRIMARY KEY,
    Username      NVARCHAR(50)  NOT NULL,
    ReceiverName  NVARCHAR(100) NOT NULL,
    Phone         NVARCHAR(20)  NOT NULL,
    Address       NVARCHAR(255) NOT NULL,
    Note          NVARCHAR(255) NULL,
    PaymentMethod VARCHAR(10)   NOT NULL CONSTRAINT DF_Orders_Pay DEFAULT 'COD',
    TotalAmount   DECIMAL(18,0) NOT NULL,
    Status        INT           NOT NULL CONSTRAINT DF_Orders_Status DEFAULT 1,
    CreatedAt     DATETIME      NOT NULL CONSTRAINT DF_Orders_Created DEFAULT GETDATE(),
    CONSTRAINT FK_Orders_Status FOREIGN KEY (Status) REFERENCES OrderStatus(StatusId)
);
GO
IF OBJECT_ID('OrderItems', 'U') IS NULL
CREATE TABLE OrderItems (
    OrderItemId INT IDENTITY(1,1) PRIMARY KEY,
    OrderId     INT           NOT NULL,
    VideoId     NVARCHAR(50)  NOT NULL,
    Title       NVARCHAR(500) NOT NULL,      -- lưu lại tên tại thời điểm đặt
    UnitPrice   DECIMAL(18,0) NOT NULL,      -- lưu lại giá tại thời điểm đặt
    Quantity    INT           NOT NULL CONSTRAINT CK_OrderItems_Qty CHECK (Quantity > 0),
    CONSTRAINT FK_OrderItems_Orders FOREIGN KEY (OrderId) REFERENCES Orders(OrderId) ON DELETE CASCADE
);
GO
